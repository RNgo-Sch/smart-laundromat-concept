package com.example.smart_laundromat_concept.ui.activities.main.booking;

import com.example.smart_laundromat_concept.data.model.AppMachine;


import android.util.Log;
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

    // Internal mapping of dryer numbers to their current state
    private final Map<Integer, AppMachine.State> dryerStates = new HashMap<>();

    /**
     * Array of resource IDs corresponding to the dryer views in the layout.
     */
    private static final int[] DRYER_IDS = {
            R.id.booking__machine_1,
            R.id.booking__machine_2,
            R.id.booking__machine_3,
            R.id.booking__machine_4
    };

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a DryerManager tied to a specific container view.
     *
     * @param container the parent view containing the dryer layout
     */
    public DryerManager(View container) {
        this.container = container;
        Log.d("DEBUG", "DryerManager container = " + container);

        // Initialize all dryers to AVAILABLE state by default
        for (int i = 1; i <= DRYER_IDS.length; i++) {
            dryerStates.put(i, AppMachine.State.AVAILABLE);
        }
    }

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Updates the logical state and UI display for a specific dryer.
     *
     * @param dryerNum 1 to 4 (1-based index)
     * @param state    the new state from {@link AppMachine.State}
     */
    public void setState(int dryerNum, AppMachine.State state) {
        dryerStates.put(dryerNum, state);
        AppMachine.setDryerState(dryerNum, state);
        updateUI(dryerNum);
    }

    /**
     * Toggles the selection outline for a specific dryer.
     *
     * @param dryerNum 1 to 4 (1-based index)
     * @param visible  true to show the blue outline, false to hide it
     */
    public void setOutline(int dryerNum, boolean visible) {
        if (dryerNum < 1 || dryerNum > DRYER_IDS.length || container == null) return;

        View view = container.findViewById(DRYER_IDS[dryerNum - 1]);
        MachineStateHelper.setOutlineVisible(view, visible);
    }

    /**
     * Refreshes the UI for all dryers managed by this instance.
     */
    public void updateAll() {
        for (int i = 1; i <= DRYER_IDS.length; i++) {
            updateUI(i);
        }
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Syncs the UI of a specific dryer with its logical state.
     *
     * @param dryerNum the machine number to update
     */
    private void updateUI(int dryerNum) {
        if (dryerNum < 1 || dryerNum > DRYER_IDS.length || container == null) return;

        // Ensure we only find views within this manager's specific container
        View view = container.findViewById(DRYER_IDS[dryerNum - 1]);

        if (view != null) {
            MachineStateHelper.setMachineState(view, dryerNum, dryerStates.get(dryerNum));
        }
    }
}