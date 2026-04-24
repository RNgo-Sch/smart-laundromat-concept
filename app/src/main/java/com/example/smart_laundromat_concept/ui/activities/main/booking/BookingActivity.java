package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.AppMachine;
import com.example.smart_laundromat_concept.data.model.QueueResponse;
import com.example.smart_laundromat_concept.data.remote.repository.MachineRepository;
import com.example.smart_laundromat_concept.data.remote.server.QueueCallback;
import com.example.smart_laundromat_concept.data.remote.server.QueueRepository;
import com.example.smart_laundromat_concept.data.session.LocationSession;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.activities.main.utils.PollingManager;
import com.example.smart_laundromat_concept.ui.common.ButtonHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.BookingNavigator;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

import static com.example.smart_laundromat_concept.data.model.AppMachine.State.*;

import java.util.Map;

/**
 * BookingActivity is the main screen where users view and book laundry machines.
 *
 * <p>The screen has two tabs — <b>Washer</b> and <b>Dryer</b> — each displaying
 * four machines with live status badges fetched from Supabase every second.
 *
 * <p><b>Key responsibilities:</b>
 * <ul>
 *   <li>Polling Supabase every 1 second via {@link PollingManager} to keep machine
 *       states up to date.</li>
 *   <li>Delegating washer UI to {@link WasherManager} and dryer UI to
 *       {@link DryerManager}, both of which extend {@link MachineManager}.</li>
 *   <li>Allowing the user to join or leave the queue via the Spring Boot backend
 *       through {@link QueueRepository}.</li>
 *   <li>Automatically highlighting the machine assigned to the logged-in user
 *       based on the {@code current_user} field returned by Supabase.</li>
 * </ul>
 *
 * <p><b>Queue state is derived entirely from Supabase</b> — no local queue flags
 * are stored in this Activity. The queue button label ("Join" vs "Leave") is
 * recalculated on every poll using {@link #isAssignedToMachine()}.
 */
public class BookingActivity extends AppCompatActivity {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /** Handles UI state for the washer section. */
    private WasherManager washerManager;

    /** Handles UI state for the dryer section. */
    private DryerManager dryerManager;

    /** Currently active tab type — drives queue button behaviour. */
    private String currentTabType = TYPE_WASHER;

    /** Handles the pull-to-refresh gesture. */
    private SwipeRefreshLayout swipeRefreshLayout;

    /** Repeatedly calls {@link #fetchMachineStates()} on a 1-second interval. */
    private PollingManager pollingManager;

    // Machine type constants
    private static final String TYPE_WASHER = "washer";
    private static final String TYPE_DRYER  = "dryer";

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the container views for each machine section
        View washerContainer = findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer  = findViewById(R.id.activity_booking__view__dryer_container);

        // Set up navigation and location UI
        MenuBarHelper.menuBar(this, MenuBarHelper.BOOKING);
        LocationHelper.setupUnderline(this);

        // Initialise machine managers — each knows how to render its own section
        washerManager = new WasherManager(washerContainer);
        dryerManager  = new DryerManager(dryerContainer);

        // Show the Washer tab by default
        new BookingNavigator().handle(this, R.id.activity_booking__btn__washer);

        // Set up the queue join/leave button
        setupQueueButton();

        // Set up the start / collect button
        updateStartButton();

        // Set up swipe-to-refresh
        swipeRefreshLayout = findViewById(R.id.activity_booking__swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::refreshAll);

        // Tab button listeners — switch between Washer and Dryer views
        View washerBtn = findViewById(R.id.activity_booking__btn__washer);
        if (washerBtn != null) {
            washerBtn.setOnClickListener(v -> {
                NavigationHelper.launchPage(this, v);
                onWasherTabSelected();
            });
        }

        View dryerBtn = findViewById(R.id.activity_booking__btn__dryer);
        if (dryerBtn != null) {
            dryerBtn.setOnClickListener(v -> {
                NavigationHelper.launchPage(this, v);
                onDryerTabSelected();
            });
        }

        // Start polling — fires fetchMachineStates() every 1 second
        pollingManager = new PollingManager(this::fetchMachineStates, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocationName();
        fetchMachineStates();
        pollingManager.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pollingManager.stop();
    }

    // -------------------------------------------------------------------------
    // Queue
    // -------------------------------------------------------------------------

    /**
     * Configures the queue button with a default label.
     * The label and action are dynamically updated on every poll via
     * {@link #updateQueueButtonState()}.
     */
    private void setupQueueButton() {
        ButtonHelper.setup(this, R.id.activity_booking__btn__queue, "Join the Queue", v -> {
            if (isAssignedToMachine()) {
                handleLeaveQueue();
            } else {
                handleJoinQueue();
            }
        });
    }

    // -------------------------------------------------------------------------
    // Start / Collect Button
    // -------------------------------------------------------------------------

    /**
     * Configures the start/collect button label, click action, and enabled state
     * based on the current state of the machine assigned to the logged-in user.
     *
     * <ul>
     *   <li>{@link AppMachine.State#IN_USE}     → "Using machine" (disabled, dimmed)</li>
     *   <li>{@link AppMachine.State#COLLECTION} → "Collect laundry" (enabled)</li>
     *   <li>Any other state                     → "Start machine" (enabled)</li>
     * </ul>
     *
     * <p>Called from {@link #updateQueueButtonState()} so it refreshes automatically
     * on every poll cycle — no manual calls needed after the first setup.
     */
    private void updateStartButton() {
        AppMachine.State assignedState = getAssignedMachineState();

        String label;
        boolean enabled;

        if (assignedState == AppMachine.State.IN_USE) {
            label   = "Using machine";
            enabled = false;
        } else if (assignedState == AppMachine.State.COLLECTION) {
            label   = "Collect laundry";
            enabled = true;
        } else {
            label   = "Start machine";
            enabled = true;
        }

        ButtonHelper.setup(this, R.id.activity_booking__btn__start, label, v -> {
            if (!enabled) return; // safety guard — should never fire when disabled

            String userIdStr = getUserIdOrFail();
            if (userIdStr == null) return;

            int machineId = getAssignedMachineId();
            if (machineId == -1) {
                Toast.makeText(this, "No machine assigned", Toast.LENGTH_SHORT).show();
                return;
            }

            QueueRepository.interact(Integer.parseInt(userIdStr), machineId, new QueueCallback() {
                @Override
                public void onSuccess(QueueResponse body) {
                    // Re-read state at callback time for the correct toast message
                    String msg = getAssignedMachineState() == AppMachine.State.COLLECTION
                            ? "Laundry collected!"
                            : "Machine started";
                    Toast.makeText(BookingActivity.this, msg, Toast.LENGTH_SHORT).show();
                    fetchMachineStates();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(BookingActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Apply visual enabled/disabled state to the button's interactive area
        View btnRoot = findViewById(R.id.activity_booking__btn__start);
        if (btnRoot != null) {
            View interactiveArea = btnRoot.findViewById(R.id.layout_button__btn__button);
            if (interactiveArea != null) {
                interactiveArea.setEnabled(enabled);
                interactiveArea.setAlpha(enabled ? 1.0f : 0.4f);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Queue Actions
    // -------------------------------------------------------------------------

    /**
     * Sends a request to the Spring Boot backend to join the queue
     * for the currently active tab's machine type.
     */
    private void handleJoinQueue() {
        String userId = getUserIdOrFail();
        if (userId == null) return;

        QueueRepository.joinQueue(userId, currentTabType, new QueueCallback() {
            @Override
            public void onSuccess(QueueResponse body) {
                Toast.makeText(BookingActivity.this, "Joined queue", Toast.LENGTH_SHORT).show();
                fetchMachineStates();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(BookingActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sends a request to the Spring Boot backend to leave the queue
     * for the currently active tab's machine type.
     */
    private void handleLeaveQueue() {
        String userId = getUserIdOrFail();
        if (userId == null) return;

        QueueRepository.cancelQueue(userId, currentTabType, new QueueCallback() {
            @Override
            public void onSuccess(QueueResponse body) {
                updateQueueButtonState();
                Toast.makeText(BookingActivity.this, "Left queue", Toast.LENGTH_SHORT).show();
                fetchMachineStates();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(BookingActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Shows the blue selection outline on the machine assigned to the current user,
     * and clears outlines on all other machines of the same type.
     *
     * @param machineType   "washer" or "dryer"
     * @param machineNumber 1 to 4
     */
    private void highlightAssignedMachine(String machineType, int machineNumber) {
        if (TYPE_WASHER.equalsIgnoreCase(machineType)) {
            for (int i = 1; i <= 4; i++) washerManager.setOutline(i, false);
            washerManager.setOutline(machineNumber, true);

        } else if (TYPE_DRYER.equalsIgnoreCase(machineType)) {
            for (int i = 1; i <= 4; i++) dryerManager.setOutline(i, false);
            dryerManager.setOutline(machineNumber, true);
        }
    }

    // -------------------------------------------------------------------------
    // UI Data
    // -------------------------------------------------------------------------

    /**
     * Fetches the latest machine states from Supabase, updates both machine sections,
     * and recalculates the queue and start button labels.
     *
     * <p>This is the core polling method. It runs every 1 second via
     * {@link PollingManager}.
     */
    private void fetchMachineStates() {
        String myUserId = UserSession.getInstance().getCurrentUser() != null
                ? String.valueOf(UserSession.getInstance().getCurrentUser().getId())
                : null;

        if (myUserId == null) return;

        MachineRepository.fetchAllMachines(() -> {

            // Find which machine (if any) is assigned to the logged-in user
            int myWasher = -1;
            int myDryer  = -1;

            for (int i = 1; i <= 4; i++) {
                AppMachine washer = AppMachine.getWashers().get(i);
                washerManager.setState(i, washer.getState());
                if (myUserId.equals(washer.getAssignedUserId())) myWasher = i;

                AppMachine dryer = AppMachine.getDryers().get(i);
                dryerManager.setState(i, dryer.getState());
                if (myUserId.equals(dryer.getAssignedUserId())) myDryer = i;
            }

            final int finalWasher = myWasher;
            final int finalDryer  = myDryer;

            runOnUiThread(() -> {
                // Highlight the user's assigned washer, or clear all washer outlines
                if (finalWasher != -1) {
                    highlightAssignedMachine(TYPE_WASHER, finalWasher);
                } else {
                    for (int i = 1; i <= 4; i++) washerManager.setOutline(i, false);
                }

                // Highlight the user's assigned dryer, or clear all dryer outlines
                if (finalDryer != -1) {
                    highlightAssignedMachine(TYPE_DRYER, finalDryer);
                } else {
                    for (int i = 1; i <= 4; i++) dryerManager.setOutline(i, false);
                }

                updateQueueButtonState();
            });
        });
    }

    /** Refreshes machine states and location name when the user swipes down. */
    private void refreshAll() {
        fetchMachineStates();
        updateLocationName();
        swipeRefreshLayout.setRefreshing(false);
    }

    /** Updates the location name label from {@link LocationSession}. */
    private void updateLocationName() {
        TextView locationName = findViewById(R.id.location_name);
        if (locationName != null) {
            locationName.setText(LocationSession.getInstance().getLocationName());
        }
    }

    /**
     * Updates the queue button label and click action based on whether the current
     * user is already assigned to a machine on the active tab.
     * <p>
     * Also triggers {@link #updateStartButton()} so both buttons stay in sync
     * after every poll cycle.
     */
    private void updateQueueButtonState() {
        ButtonHelper.setup(this,
                R.id.activity_booking__btn__queue,
                isAssignedToMachine() ? "Leave Queue" : "Join the Queue",
                v -> {
                    if (isAssignedToMachine()) handleLeaveQueue();
                    else                       handleJoinQueue();
                });

        updateStartButton(); // keep start/collect/disabled label in sync
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns the logged-in user's ID as a String.
     * Shows a toast and returns null if the session has expired.
     *
     * @return user ID string, or null if not logged in
     */
    private String getUserIdOrFail() {
        if (UserSession.getInstance().getCurrentUser() != null) {
            return String.valueOf(UserSession.getInstance().getCurrentUser().getId());
        }
        Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
        return null;
    }

    /**
     * Checks whether the logged-in user is currently assigned to any machine
     * of the active tab type, based on live Supabase data.
     *
     * @return true if the user has an assigned machine on the current tab
     */
    private boolean isAssignedToMachine() {
        String myUserId = getUserIdOrFail();
        if (myUserId == null) return false;

        Map<Integer, AppMachine> machines = TYPE_WASHER.equalsIgnoreCase(currentTabType)
                ? AppMachine.getWashers()
                : AppMachine.getDryers();

        for (int i = 1; i <= 4; i++) {
            if (myUserId.equals(machines.get(i).getAssignedUserId())) return true;
        }
        return false;
    }

    /**
     * Returns the current {@link AppMachine.State} of the machine assigned to the
     * logged-in user on the active tab, or {@code null} if no machine is assigned.
     *
     * <p>Used by {@link #updateStartButton()} to determine the correct button label
     * and enabled state without iterating the machine map more than once.
     *
     * @return assigned machine state, or null
     */
    private AppMachine.State getAssignedMachineState() {
        String myUserId = UserSession.getInstance().getCurrentUser() != null
                ? String.valueOf(UserSession.getInstance().getCurrentUser().getId())
                : null;
        if (myUserId == null) return null;

        Map<Integer, AppMachine> machines = TYPE_WASHER.equalsIgnoreCase(currentTabType)
                ? AppMachine.getWashers()
                : AppMachine.getDryers();

        for (AppMachine machine : machines.values()) {
            if (myUserId.equals(machine.getAssignedUserId())) {
                return machine.getState();
            }
        }
        return null;
    }

    /**
     * Returns the machine ID (1–4) assigned to the current user on the active tab.
     * Returns -1 if no machine is assigned.
     *
     * @return 1-based machine number, or -1
     */
    private int getAssignedMachineId() {
        String myUserId = getUserIdOrFail();
        if (myUserId == null) return -1;

        Map<Integer, AppMachine> machines = TYPE_WASHER.equalsIgnoreCase(currentTabType)
                ? AppMachine.getWashers()
                : AppMachine.getDryers();

        for (int i = 1; i <= 4; i++) {
            if (myUserId.equals(machines.get(i).getAssignedUserId())) {
                return i;
            }
        }
        return -1;
    }

    // -------------------------------------------------------------------------
    // Tab Callbacks
    // -------------------------------------------------------------------------

    /** Called when the user taps the Washer tab button. */
    public void onWasherTabSelected() {
        currentTabType = TYPE_WASHER;
        updateQueueButtonState();
    }

    /** Called when the user taps the Dryer tab button. */
    public void onDryerTabSelected() {
        currentTabType = TYPE_DRYER;
        updateQueueButtonState();
    }

    // -------------------------------------------------------------------------
    // Accessors (used by BookingNavigator)
    // -------------------------------------------------------------------------

    /** @return the WasherManager for this activity */
    public WasherManager getWasherManager() { return washerManager; }

    /** @return the DryerManager for this activity */
    public DryerManager  getDryerManager()  { return dryerManager; }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    /**
     * Delegates all navigation events to {@link NavigationHelper}.
     */
    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}