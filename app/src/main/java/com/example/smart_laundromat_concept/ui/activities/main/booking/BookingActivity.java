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
import com.example.smart_laundromat_concept.data.model.QueueResponse;
import com.example.smart_laundromat_concept.data.remote.BackendClient;
import com.example.smart_laundromat_concept.data.remote.QueueRepository;
import com.example.smart_laundromat_concept.data.session.LocationSession;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.data.model.AppMachine;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.common.ButtonHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.BookingNavigator;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.smart_laundromat_concept.data.model.AppMachine.State.*;

import java.util.Map;

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

    /** Tracks whether the current user is in the queue. */
    private boolean isInWasherQueue = false;
    private boolean isInDryerQueue = false;
    private String currentTabType = "washer";

    /** Handles the pull-to-refresh gesture on the booking screen. */
    private SwipeRefreshLayout swipeRefreshLayout;

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
            if (currentTabType.equals("washer")) {
                if (isInWasherQueue) handleLeaveQueue();
                else handleJoinQueue();
            } else {
                if (isInDryerQueue) handleLeaveQueue();
                else handleJoinQueue();
            }
        });
    }
    private AppMachine.State mapState(String status) {
        switch (status) {
            case "AVAILABLE": return AVAILABLE;
            case "RESERVED": return RESERVED;
            case "IN_USE": return IN_USE;
            case "OOS": return OOS;
            default: return AVAILABLE;
        }
    }

    /**
     * Sends a request to join the queue and updates the button label on success.
     */

    private void handleJoinQueue() {

        String userId = UserSession.getInstance().getUsername();

        if (userId == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = currentTabType;

        if (type.equals("washer") && isInWasherQueue) return;
        if (type.equals("dryer") && isInDryerQueue) return;

        QueueRepository.joinQueue(userId, type, new Callback<QueueResponse>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {
                if (isFinishing() || isDestroyed()) return;

                if (response.isSuccessful() && response.body() != null) {
                    QueueResponse body = response.body();
                    if (body.machineType != null && body.machineType.equalsIgnoreCase("washer")) {
                        isInWasherQueue = true;
                    } else if (body.machineType != null && body.machineType.equalsIgnoreCase("dryer")) {
                        isInDryerQueue = true;
                    }

                    highlightAssignedMachine(body.machineType, body.machineNumber);

                    updateQueueButtonState();

                    Toast.makeText(BookingActivity.this, body.message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookingActivity.this, "Failed to join queue", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(BookingActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLeaveQueue() {

        String userId = UserSession.getInstance().getUsername();

        if (userId == null) {
            Toast.makeText(this, "No active queue found", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = currentTabType;
        QueueRepository.cancelQueue(userId, type, new Callback<QueueResponse>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {
                if (isFinishing() || isDestroyed()) return;

                if (response.isSuccessful() && response.body() != null) {
                    if (type.equalsIgnoreCase("washer")) {
                        isInWasherQueue = false;
                    } else if (type.equalsIgnoreCase("dryer")) {
                        isInDryerQueue = false;
                    }

                    clearOutlinesForType(type);

                    updateQueueButtonState();

                    Toast.makeText(BookingActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookingActivity.this, "Failed to leave queue", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(BookingActivity.this, "Network error", Toast.LENGTH_SHORT).show();
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

        if (machineType.equalsIgnoreCase("washer")) {
            // Clear only washer outlines, keep dryer intact
            for (int i = 1; i <= 4; i++) {
                washerManager.setOutline(i, false);
            }
            washerManager.setOutline(machineNumber, true);
            washerManager.setState(machineNumber, RESERVED);

        } else if (machineType.equalsIgnoreCase("dryer")) {
            // Clear only dryer outlines, keep washer intact
            for (int i = 1; i <= 4; i++) {
                dryerManager.setOutline(i, false);
            }
            dryerManager.setOutline(machineNumber, true);
            dryerManager.setState(machineNumber, RESERVED);
        }
    }

    /**
     * Clears all outlines on all machines.
     * Called when the user leaves the queue.
     */
    private void clearAllOutlines() {
        for (int i = 1; i <= 4; i++) {
            washerManager.setOutline(i, false);
            dryerManager.setOutline(i, false);
        }
    }

    private void clearOutlinesForType(String type) {
        for (int i = 1; i <= 4; i++) {
            if (type.equalsIgnoreCase("washer")) {
                washerManager.setOutline(i, false);
            } else if (type.equalsIgnoreCase("dryer")) {
                dryerManager.setOutline(i, false);
            }
        }
    }

    private void fetchMachineStates() {
        BackendClient.getApi().getMachines().enqueue(new Callback<Map<String, Map<Integer, String>>>() {
            @Override
            public void onResponse(Call<Map<String, Map<Integer, String>>> call,
                                   Response<Map<String, Map<Integer, String>>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Map<String, Map<Integer, String>> data = response.body();

                    Map<Integer, String> washers = data.get("washer");
                    Map<Integer, String> dryers  = data.get("dryer");

                    // Update washer UI
                    for (int i = 1; i <= 4; i++) {
                        String state = washers.get(i);
                        washerManager.setState(i, parseState(state));
                    }

                    // Update dryer UI
                    for (int i = 1; i <= 4; i++) {
                        String state = dryers.get(i);
                        dryerManager.setState(i, parseState(state));
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Map<Integer, String>>> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Failed to load machines", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private AppMachine.State parseState(String state) {
        if (state == null) return AVAILABLE;

        switch (state) {
            case "RESERVED": return RESERVED;
            case "IN_USE": return IN_USE;
            case "OOS": return OOS;
            default: return AVAILABLE;
        }
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
        if (currentTabType.equals("washer") && isInWasherQueue) {
            ButtonHelper.setup(this,
                    R.id.activity_booking__btn__queue,
                    "Leave Queue",
                    v -> handleLeaveQueue());
        } else if (currentTabType.equals("dryer") && isInDryerQueue) {
            ButtonHelper.setup(this,
                    R.id.activity_booking__btn__queue,
                    "Leave Queue",
                    v -> handleLeaveQueue());
        } else {
            ButtonHelper.setup(this,
                    R.id.activity_booking__btn__queue,
                    "Join the Queue",
                    v -> handleJoinQueue());
        }
    }
}