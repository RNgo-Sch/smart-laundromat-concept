package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.smart_laundromat_concept.data.remote.supabase.SupabaseRealtime;
import com.example.smart_laundromat_concept.data.session.LocationSession;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.activities.main.utils.PollingManager;
import com.example.smart_laundromat_concept.ui.common.ButtonHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.BookingNavigator;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

import static com.example.smart_laundromat_concept.data.model.AppMachine.State.*;

/**
 * BookingActivity manages the machine selection and status display for the laundromat.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Coordinating {@link WasherManager} and {@link DryerManager} to display machine states.</li>
 *   <li>Initializing UI components like the menu bar and location markers.</li>
 *   <li>Handling navigation to specific machine pages via {@link NavigationHelper}.</li>
 *   <li>Handling queue join/cancel via {@link QueueRepository}.</li>
 * </ul>
 * <p>
 * Queue state is derived directly from Supabase via the {@code current_user} field —
 * no local queue state is tracked in this Activity.
 */
public class BookingActivity extends AppCompatActivity {

    // Machine managers to handle UI and state logic for each section
    private WasherManager washerManager;
    private DryerManager  dryerManager;

    // Constants for machine types
    private static final String TYPE_WASHER = "washer";
    private static final String TYPE_DRYER  = "dryer";

    /** Currently visible tab — drives queue button label and join/leave logic. */
    private String currentTabType = TYPE_WASHER;

    /** Handles the pull-to-refresh gesture on the booking screen. */
    private SwipeRefreshLayout swipeRefreshLayout;
    private PollingManager pollingManager;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns the current user's ID as a String, or shows a toast and returns null
     * if the session has expired.
     */
    private String getUserIdOrFail() {
        if (UserSession.getInstance().getCurrentUser() != null) {
            return String.valueOf(UserSession.getInstance().getCurrentUser().getId());
        }
        Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
        return null;
    }

    /**
     * Checks whether the logged-in user is currently assigned to any machine
     * of the active tab type. Derived from Supabase data — no local state needed.
     *
     * @return true if the user has an assigned machine on the current tab
     */
    private boolean isAssignedToMachine() {
        String myUserId = getUserIdOrFail();
        if (myUserId == null) return false;

        if (TYPE_WASHER.equalsIgnoreCase(currentTabType)) {
            for (int i = 1; i <= 4; i++) {
                if (myUserId.equals(AppMachine.getWashers().get(i).getAssignedUserId())) return true;
            }
        } else {
            for (int i = 1; i <= 4; i++) {
                if (myUserId.equals(AppMachine.getDryers().get(i).getAssignedUserId())) return true;
            }
        }
        return false;
    }

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

        // 1. Start polling Supabase for live machine states
        pollingManager = new PollingManager(
                this::fetchMachineStates,
                1000 // 1 second
        );

        // Initial load (VERY IMPORTANT)
//        fetchMachineStates();
//
//        // Realtime updates
//        SupabaseRealtime.subscribeToMachines();


        // 2. Retrieve container views for washer and dryer sections
        View washerContainer = findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer  = findViewById(R.id.activity_booking__view__dryer_container);

        // 3. Initialize UI styling helpers
        MenuBarHelper.menuBar(this, MenuBarHelper.BOOKING);
        LocationHelper.setupUnderline(this);

        // 4. Initialize machine managers
        washerManager = new WasherManager(washerContainer);
        dryerManager  = new DryerManager(dryerContainer);

        // 5. Set default visible tab (Washer view)
        new BookingNavigator().handle(this, R.id.activity_booking__btn__washer);
        currentTabType = TYPE_WASHER;

        // 6. Setup Queue button
        setupQueueButton();

        // 7. Demo button — populates machine states for presentation purposes
        ButtonHelper.setup(this, R.id.activity_booking__btn__populate, "Populate Demo", v -> {
            populateDemoData();
            Toast.makeText(this, "Demo populated", Toast.LENGTH_SHORT).show();
        });

        // 8. Initialize swipe to refresh
        swipeRefreshLayout = findViewById(R.id.activity_booking__swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::refreshAll);

        // 9. Tab button listeners
        View washerBtn = findViewById(R.id.activity_booking__btn__washer);
        if (washerBtn != null) {
            washerBtn.setOnClickListener(v -> {
                new BookingNavigator().handle(this, R.id.activity_booking__btn__washer);
                onWasherTabSelected();
            });
        }

        View dryerBtn = findViewById(R.id.activity_booking__btn__dryer);
        if (dryerBtn != null) {
            dryerBtn.setOnClickListener(v -> {
                new BookingNavigator().handle(this, R.id.activity_booking__btn__dryer);
                onDryerTabSelected();
            });
        }
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
     * Sets up the queue button. Label and action are derived from Supabase state.
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

    /**
     * Sends a request to join the queue for the current tab's machine type.
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
     * Sends a request to leave the queue for the current tab's machine type.
     */
    private void handleLeaveQueue() {
        String userId = getUserIdOrFail();
        if (userId == null) return;

        QueueRepository.cancelQueue(userId, currentTabType, new QueueCallback() {
            @Override
            public void onSuccess(QueueResponse body) {
                updateQueueButtonState();
                Toast.makeText(BookingActivity.this, body.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(BookingActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Highlights the machine assigned to the current user by showing a blue outline.
     * Clears all other outlines of the same type first.
     *
     * @param machineType   "washer" or "dryer"
     * @param machineNumber 1 to 4
     */
    private void highlightAssignedMachine(String machineType, int machineNumber) {
        if (machineType == null) return;

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

    private void refreshAll() {
        fetchMachineStates();
        updateLocationName();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void updateLocationName() {
        TextView locationName = findViewById(R.id.location_name);
        if (locationName != null) {
            locationName.setText(LocationSession.getInstance().getLocationName());
        }
    }

    /**
     * Fetches machine states from Supabase, updates the UI, and highlights
     * any machine currently assigned to the logged-in user.
     * Also refreshes the queue button label based on the latest Supabase state.
     */
    private void fetchMachineStates() {
        String myUserId = getUserIdOrFail();
        if (myUserId == null) return;

        MachineRepository.fetchAllMachines(() -> {

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
                // Washer — highlight if assigned, clear all outlines if not
                if (finalWasher != -1) {
                    highlightAssignedMachine(TYPE_WASHER, finalWasher);
                } else {
                    for (int i = 1; i <= 4; i++) washerManager.setOutline(i, false);
                }

                // Dryer — highlight if assigned, clear all outlines if not
                if (finalDryer != -1) {
                    highlightAssignedMachine(TYPE_DRYER, finalDryer);
                } else {
                    for (int i = 1; i <= 4; i++) dryerManager.setOutline(i, false);
                }

                updateQueueButtonState();
            });
        });
    }


    // -------------------------------------------------------------------------
    // Demo
    // -------------------------------------------------------------------------

    private void populateDemoData() {
        washerManager.setState(1, RESERVED);
        washerManager.setState(2, IN_USE);
        washerManager.setState(3, IN_USE);
        washerManager.setState(4, OOS);

        dryerManager.setState(1, IN_USE);
        dryerManager.setState(2, IN_USE);
        dryerManager.setState(3, OOS);
        dryerManager.setState(4, IN_USE);

        UserSession.getInstance().setActiveBooking("Washer", 2, 30 * 60 * 1000L);


        highlightAssignedMachine(TYPE_WASHER, 1);
        dryerManager.setOutline(3, true);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public WasherManager getWasherManager() { return washerManager; }
    public DryerManager  getDryerManager()  { return dryerManager; }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }

    public void onWasherTabSelected() {
        currentTabType = TYPE_WASHER;
        updateQueueButtonState();
    }

    public void onDryerTabSelected() {
        currentTabType = TYPE_DRYER;
        updateQueueButtonState();
    }

    /**
     * Refreshes the queue button label based on current Supabase state.
     * "Leave Queue" if the user is assigned to a machine, "Join the Queue" otherwise.
     */
    private void updateQueueButtonState() {
        ButtonHelper.setup(this,
                R.id.activity_booking__btn__queue,
                isAssignedToMachine() ? "Leave Queue" : "Join the Queue",
                v -> {
                    if (isAssignedToMachine()) handleLeaveQueue();
                    else                       handleJoinQueue();
                });
    }
}