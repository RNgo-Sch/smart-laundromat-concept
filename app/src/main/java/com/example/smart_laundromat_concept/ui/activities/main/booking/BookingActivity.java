package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
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
 * <p>
 * - Coordinating {@link WasherManager} and {@link DryerManager} to display machine states.
 * <p>
 * - Initializing UI components like the menu bar and location markers.
 * <p>
 * - Handling navigation to specific machine pages via {@link NavigationHelper}.
 * <p>
 * This activity uses a "Manager" pattern to handle different categories of machines (Washers/Dryers)
 * that share the same layout IDs but exist in different container views.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link WasherManager} or {@link MachineStateHelper}
 * to jump to their respective implementations.
 */
public class BookingActivity extends AppCompatActivity {

    // Machine managers to handle UI and state logic for each section
    private WasherManager washerManager;
    private DryerManager dryerManager;

    /**
     * Initializes the Activity, sets up the layout, and configures the machine managers.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display (system bars overlap with app content)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        // Adjust padding to avoid UI elements being hidden behind system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Retrieve container views for washer and dryer sections.
        // These containers allow managers to find sub-views without ID collisions.
        View washerContainer = findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer = findViewById(R.id.activity_booking__view__dryer_container);

        // 2. Initialize UI styling helpers (menu bar + location underline styling)
        MenuBarHelper.menuBar(this, MenuBarHelper.BOOKING);
        LocationHelper.setupUnderline(this);

        // 3. Initialize machine managers responsible for handling UI + state logic
        washerManager = new WasherManager(washerContainer);
        dryerManager = new DryerManager(dryerContainer);

        // 4. Configure initial machine states (mock/demo data)
        setupInitialStates();

        // 5. Set default visible tab (Washer view) using the BookingNavigator
        new BookingNavigator().handle(this, R.id.activity_booking__btn__washer);

        // 6. Setup Queue button interaction with the ButtonHelper
        ButtonHelper.setup(this, R.id.activity_booking__btn__queue, "Join the Queue", v -> {
            Toast.makeText(this, "Join the Queue!", Toast.LENGTH_SHORT).show();
        });

        ButtonHelper.setup(this, R.id.activity_booking__btn__populate, "populate", v -> {
            Toast.makeText(this, "populate", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Initializes the default states of all washer and dryer machines.
     * This simulates backend data for UI display purposes.
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
// Example: Washer 2, 30-minute cycle
        UserSession.getInstance().setActiveBooking("Washer", 2, 30 * 60 * 1000L);
        // Highlight specific machines to demonstrate the selection outline effect
        washerManager.setOutline(1, true);
        dryerManager.setOutline(3, true);
    }

    /**
     * Provides access to the WasherManager.
     * @return The WasherManager instance.
     */
    public WasherManager getWasherManager() {
        return washerManager;
    }

    /**
     * Provides access to the DryerManager.
     * @return The DryerManager instance.
     */
    public DryerManager getDryerManager() {
        return dryerManager;
    }

    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     * <p>
     * (Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage} to jump to the method)
     */
    public void launchPage(View view){
        NavigationHelper.launchPage(this, view);
    }
}
