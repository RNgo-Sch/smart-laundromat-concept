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
import com.example.smart_laundromat_concept.data.model.MachineData;
import com.example.smart_laundromat_concept.data.model.QueueResponse;
import com.example.smart_laundromat_concept.data.remote.QueueCallback;
import com.example.smart_laundromat_concept.data.remote.QueueRepository;
import com.example.smart_laundromat_concept.data.session.LocationSession;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.common.ButtonHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.BookingNavigator;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;


import static com.example.smart_laundromat_concept.data.model.AppMachine.State.*;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
 */
public class BookingActivity extends AppCompatActivity {

    // Machine managers to handle UI and state logic for each section
    private WasherManager washerManager;
    private DryerManager dryerManager;

    // Constants for machine types
    private static final String TYPE_WASHER = "washer";
    private static final String TYPE_DRYER = "dryer";

    /** Tracks whether the current user is in the queue. */
    private boolean isInWasherQueue = false;
    private boolean isInDryerQueue = false;
    private String currentTabType = TYPE_WASHER;
    private int assignedMachineNumber = -1;

    /** Handles the pull-to-refresh gesture on the booking screen. */
    private SwipeRefreshLayout swipeRefreshLayout;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private String getUserIdOrFail() {
        if (UserSession.getInstance().getCurrentUser() != null) {
            return String.valueOf(UserSession.getInstance().getCurrentUser().getId());
        } else {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private boolean isInQueue() {
        return (TYPE_WASHER.equalsIgnoreCase(currentTabType) && isInWasherQueue)
                || (TYPE_DRYER.equalsIgnoreCase(currentTabType) && isInDryerQueue);
    }

    private void setQueueState(String type, boolean value) {
        if (TYPE_WASHER.equalsIgnoreCase(type)) {
            isInWasherQueue = value;
        } else if (TYPE_DRYER.equalsIgnoreCase(type)) {
            isInDryerQueue = value;
        }
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

        // 1. Retrieve container views for washer and dryer sections
        View washerContainer = findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer  = findViewById(R.id.activity_booking__view__dryer_container);

        // 2. Initialize UI styling helpers
        MenuBarHelper.menuBar(this, MenuBarHelper.BOOKING);
        LocationHelper.setupUnderline(this);

        // 3. Initialize machine managers
        washerManager = new WasherManager(washerContainer);
        dryerManager  = new DryerManager(dryerContainer);

        // 4. Load current machine states from MachineData
        fetchMachineStates();

        // 5. Set default visible tab (Washer view)
        new BookingNavigator().handle(this, R.id.activity_booking__btn__washer);
        currentTabType = "washer";

        // 6. Setup Queue button
        setupQueueButton();

        // Demo button — populates machine states for presentation purposes
        ButtonHelper.setup(this, R.id.activity_booking__btn__populate, "Populate Demo", v -> {
            populateDemoData();
            Toast.makeText(this, "Demo populated", Toast.LENGTH_SHORT).show();
        });

        // 7. Initialize swipe to refresh
        swipeRefreshLayout = findViewById(R.id.activity_booking__swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::refreshAll);

        // Add tab selection calls after button handlers
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

    }

    // -------------------------------------------------------------------------
    // Queue
    // -------------------------------------------------------------------------

    /**
     * Sets up the queue button with toggle behavior.
     * First press = join queue, second press = leave queue.
     */
    private void setupQueueButton() {
        ButtonHelper.setup(this, R.id.activity_booking__btn__queue, "Join the Queue", v -> {
            if (isInQueue()) {
                handleLeaveQueue();
            } else {
                handleJoinQueue();
            }
        });
    }

    /**
     * Sends a request to join the queue and updates the button label on success.
     */

    private void handleJoinQueue() {

        String userId = getUserIdOrFail();
        if (userId == null) return;

        String machineType = currentTabType;

        // Call repository (clean architecture)
        QueueRepository.joinQueue(userId, machineType, new QueueCallback() {
            @Override
            public void onSuccess(QueueResponse body) {
                handleJoinQueueSuccess(body);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(BookingActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLeaveQueue() {
        String userId = getUserIdOrFail();
        if (userId == null) return;

        String type = currentTabType;

        QueueRepository.cancelQueue(userId, type, new QueueCallback() {
            @Override
            public void onSuccess(QueueResponse body) {
                setQueueState(type, false);
                clearAssignedMachine(type);
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
     * Highlights the machine assigned by the backend.
     *
     * @param machineType   "washer" or "dryer"
     * @param machineNumber 1 to 4
     */
    private void highlightAssignedMachine(String machineType, int machineNumber) {
        if (machineType == null) return;

        if (TYPE_WASHER.equalsIgnoreCase(machineType)) {
            // Clear only washer outlines, keep dryer intact
            for (int i = 1; i <= 4; i++) {
                washerManager.setOutline(i, false);
            }
            washerManager.setOutline(machineNumber, true);
            washerManager.setState(machineNumber, RESERVED);

        } else if (TYPE_DRYER.equalsIgnoreCase(machineType)) {
            // Clear only dryer outlines, keep washer intact
            for (int i = 1; i <= 4; i++) {
                dryerManager.setOutline(i, false);
            }
            dryerManager.setOutline(machineNumber, true);
            dryerManager.setState(machineNumber, RESERVED);
        }
    }
    private void handleJoinQueueSuccess(QueueResponse body) {
        assignedMachineNumber = body.machineNumber;
        setQueueState(body.machineType, true);
        highlightAssignedMachine(body.machineType, body.machineNumber);
        updateQueueButtonState();
        Toast.makeText(this, body.message, Toast.LENGTH_SHORT).show();
    }


    private void clearAssignedMachine(String type) {
        if (assignedMachineNumber == -1) return;

        if (TYPE_WASHER.equalsIgnoreCase(type)) {
            washerManager.setOutline(assignedMachineNumber, false);
            washerManager.setState(assignedMachineNumber, AVAILABLE);
        } else if (TYPE_DRYER.equalsIgnoreCase(type)) {
            dryerManager.setOutline(assignedMachineNumber, false);
            dryerManager.setState(assignedMachineNumber, AVAILABLE);
        }
        assignedMachineNumber = -1;
    }

    // -------------------------------------------------------------------------
    // Setup
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

        washerManager.setOutline(1, true);
        dryerManager.setOutline(3, true);

        sendBroadcast(new android.content.Intent("MACHINE_UPDATED"));
    }

    // -------------------------------------------------------------------------
    // UI Data
    // -------------------------------------------------------------------------

    private void refreshAll() {
        washerManager.updateAll();
        dryerManager.updateAll();
        updateLocationName();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void updateLocationName() {
        TextView locationName = findViewById(R.id.location_name);
        if (locationName != null) {
            locationName.setText(LocationSession.getInstance().getLocationName());
        }
    }

    private void fetchMachineStates() {
        QueueRepository.getMachines(new Callback<Map<String, Map<Integer, String>>>() {
            @Override
            public void onResponse(Call<Map<String, Map<Integer, String>>> call,
                                   Response<Map<String, Map<Integer, String>>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Map<String, Map<Integer, String>> data = response.body();

                    Map<Integer, String> washers = data.get(TYPE_WASHER);
                    Map<Integer, String> dryers  = data.get(TYPE_DRYER);

                    if (washers == null || dryers == null) return;

                    for (int i = 1; i <= 4; i++) {
                        AppMachine.State washerState = parseState(washers.get(i));
                        AppMachine.State dryerState  = parseState(dryers.get(i));

                        updateWasher(i, washerState);
                        updateDryer(i, dryerState);
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Map<Integer, String>>> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Failed to load machines", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateWasher(int i, AppMachine.State state) {
        washerManager.setState(i, state);
        MachineData.setWasherState(i, state);
    }

    private void updateDryer(int i, AppMachine.State state) {
        dryerManager.setState(i, state);
        MachineData.setDryerState(i, state);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public WasherManager getWasherManager() { return washerManager; }
    public DryerManager getDryerManager()   { return dryerManager; }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }

    public void onWasherTabSelected() {
        currentTabType = "washer";
        updateQueueButtonState();
    }

    public void onDryerTabSelected() {
        currentTabType = "dryer";
        updateQueueButtonState();
    }

    private void updateQueueButtonState() {


        ButtonHelper.setup(this,
                R.id.activity_booking__btn__queue,
                isInQueue() ? "Leave Queue" : "Join the Queue",
                v -> {
                    if (isInQueue()) {
                        handleLeaveQueue();
                    } else {
                        handleJoinQueue();
                    }
                });
    }

    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------

    private AppMachine.State parseState(String state) {
        if (state == null) return AVAILABLE;

        switch (state) {
            case "RESERVED": return RESERVED;
            case "IN_USE": return IN_USE;
            case "OOS": return OOS;
            default: return AVAILABLE;
        }
    }
}