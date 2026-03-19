package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.view.View;
import com.example.smart_laundromat_concept.R;

import java.util.HashMap;
import java.util.Map;

/**
 * DryerManager is responsible for managing the UI state and selection of dryers.
 * <p>
 * It encapsulates the logic for finding dryer-specific views within a shared layout
 * and applying state updates via {@link MachineStateHelper}.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link MachineStateHelper#setMachineState} 
 * to jump directly to the UI update implementation.
 */
public class DryerManager {

    // The container view holding the dryer layouts
    private final View container;
    
    // Internal mapping of dryer numbers to their current state constants
    private final Map<Integer, Integer> dryerStates = new HashMap<>();

    /**
     * Array of resource IDs corresponding to the dryer views in the layout.
     */
    private static final int[] DRYER_IDS = {
            R.id.booking__machine_1,
            R.id.booking__machine_2,
            R.id.booking__machine_3,
            R.id.booking__machine_4
    };

    /**
     * Constructs a DryerManager tied to a specific container view.
     *
     * @param container The parent view containing the dryer layout (e.g., the included dryer container).
     */
    public DryerManager(View container) {
        this.container = container;
        // Initialize all dryers to OPEN state by default
        for (int i = 1; i <= DRYER_IDS.length; i++) {
            dryerStates.put(i, MachineStateHelper.STATE_OPEN);
        }
    }

    /**
     * Updates the logical state and UI display for a specific dryer.
     *
     * @param dryerNum 1 to 4 (1-based index).
     * @param state    The new state constant from {@link MachineStateHelper}.
     */
    public void setState(int dryerNum, int state) {
        dryerStates.put(dryerNum, state);
        updateUI(dryerNum);
    }

    /**
     * Toggles the selection outline for a specific dryer.
     *
     * @param dryerNum 1 to 4 (1-based index).
     * @param visible  True to show the blue outline, false to hide it.
     */
    public void setOutline(int dryerNum, boolean visible) {
        if (dryerNum < 1 || dryerNum > DRYER_IDS.length || container == null) return;
        View view = container.findViewById(DRYER_IDS[dryerNum - 1]);
        MachineStateHelper.setOutlineVisible(view, visible);
    }

    /**
     * Internal helper to sync the UI of a specific dryer with its logical state.
     *
     * @param dryerNum The machine number to update.
     */
    private void updateUI(int dryerNum) {
        if (dryerNum < 1 || dryerNum > DRYER_IDS.length || container == null) return;

        int resId = DRYER_IDS[dryerNum - 1]; // Array is 0-indexed
        // Ensure we only find views within this manager's specific container
        View view = container.findViewById(resId);
        
        if (view != null) {
            MachineStateHelper.setMachineState(view, dryerNum, dryerStates.get(dryerNum));
        }
    }

    /**
     * Refreshes the UI for all dryers managed by this instance.
     */
    public void updateAll() {
        for (int i = 1; i <= DRYER_IDS.length; i++) {
            updateUI(i);
        }
    }
}
