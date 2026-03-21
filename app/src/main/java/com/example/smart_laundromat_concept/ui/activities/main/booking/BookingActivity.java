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
import com.example.smart_laundromat_concept.data.session.LocationSession;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.common.ButtonHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.BookingNavigator;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

/**
 * BookingActivity manages the machine selection and status display for the laundromat.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Coordinating {@link WasherManager} and {@link DryerManager} to display machine states.</li>
 *   <li>Initializing UI components like the menu bar and location markers.</li>
 *   <li>Handling navigation to specific machine pages via {@link NavigationHelper}.</li>
 * </ul>
 * <p>
 * This activity uses a "Manager" pattern to handle different categories of machines
 * (Washers/Dryers) that share the same layout IDs but exist in different container views.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link WasherManager} or
 * {@link MachineStateHelper} to jump to their respective implementations.
 */
public class BookingActivity extends AppCompatActivity {

    // Machine managers to handle UI and state logic for each section
    private WasherManager washerManager;
    private DryerManager dryerManager;

    /** Handles the pull-to-refresh gesture on the booking screen. */
    private SwipeRefreshLayout swipeRefreshLayout;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Initializes the Activity, sets up the layout, and configures the machine managers.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        // Adjust padding to avoid UI elements being hidden behind system bars
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

        // 4. Configure initial machine states
        setupInitialStates();

        // 5. Set default visible tab (Washer view)
        new BookingNavigator().handle(this, R.id.activity_booking__btn__washer);

        // 6. Setup Queue button
        ButtonHelper.setup(this, R.id.activity_booking__btn__queue, "Join the Queue", v -> {
            Toast.makeText(this, "Join the Queue!", Toast.LENGTH_SHORT).show();
        });

        // Demo button — populates machine states for presentation purposes
        ButtonHelper.setup(this, R.id.activity_booking__btn__populate, "Populate Demo", v -> {
            Toast.makeText(this, "populate", Toast.LENGTH_SHORT).show();
        });

        // 7. Initialize swipe to refresh
        swipeRefreshLayout = findViewById(R.id.activity_booking__swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> refreshAll());
    }

    /**
     * Called every time the screen becomes visible.
     * Refreshes the location name in case the user changed it in LocationActivity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateLocationName();
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    /**
     * Initializes the default states of all washer and dryer machines.
     * TODO: Replace mock data with real Supabase data before submission.
     * <p>
     * (Hold Cmd/Ctrl + Click on {@link WasherManager#setState} to jump to the logic)
     */
    private void setupInitialStates() {
        // Mock data for Washer states
        washerManager.setState(1, MachineStateHelper.STATE_RESERVED);
        washerManager.setState(2, MachineStateHelper.STATE_IN_USE);
        washerManager.setState(3, MachineStateHelper.STATE_OPEN);
        washerManager.setState(4, MachineStateHelper.STATE_OUT_OF_SERVICE);

        // Mock data for Dryer states
        dryerManager.setState(1, MachineStateHelper.STATE_OPEN);
        dryerManager.setState(2, MachineStateHelper.STATE_COLLECTION);
        dryerManager.setState(3, MachineStateHelper.STATE_OUT_OF_SERVICE);
        dryerManager.setState(4, MachineStateHelper.STATE_IN_USE);

        // TODO: Replace with real booking data from Supabase
        UserSession.getInstance().setActiveBooking("Washer", 2, 30 * 60 * 1000L);

        // Highlight selected machines
        washerManager.setOutline(1, true);
        dryerManager.setOutline(3, true);
    }

    // -------------------------------------------------------------------------
    // UI Data
    // -------------------------------------------------------------------------

    /**
     * Refreshes all data on the booking screen.
     * Called when the user pulls down to refresh.
     */
    private void refreshAll() {
        washerManager.updateAll();
        dryerManager.updateAll();
        updateLocationName();

        // Stop the spinning indicator after refresh
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Updates the location name in the floating location bar.
     * Called in onResume so it refreshes every time the user returns from LocationActivity.
     */
    private void updateLocationName() {
        TextView locationName = findViewById(R.id.location_name);
        if (locationName != null) {
            locationName.setText(LocationSession.getInstance().getLocationName());
        }
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    /**
     * Provides access to the WasherManager.
     *
     * @return the WasherManager instance
     */
    public WasherManager getWasherManager() {
        return washerManager;
    }

    /**
     * Provides access to the DryerManager.
     *
     * @return the DryerManager instance
     */
    public DryerManager getDryerManager() {
        return dryerManager;
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     * <p>
     * (Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage} to jump to the method)
     */
    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}