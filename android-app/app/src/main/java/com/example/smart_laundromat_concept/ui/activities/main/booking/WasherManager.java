package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.view.View;

import com.example.smart_laundromat_concept.R;

import java.util.HashMap;
import java.util.Map;

/**
 * WasherManager is responsible for managing the UI state and selection of washers.
 * <p>
 * It encapsulates the logic for finding washer-specific views within a shared layout
 * and applying state updates via {@link MachineStateHelper}.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link MachineStateHelper#setMachineState}
 * to jump directly to the UI update implementation.
 */
public class WasherManager {

    // The container view holding the washer layouts
    private final View container;

    // Internal mapping of washer numbers to their current state constants
    private final Map<Integer, Integer> washerStates = new HashMap<>();

    /**
     * Array of resource IDs corresponding to the washer views in the layout.
     */
    private static final int[] WASHER_IDS = {
            R.id.booking__machine_1,
            R.id.booking__machine_2,
            R.id.booking__machine_3,
            R.id.booking__machine_4
    };

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a WasherManager tied to a specific container view.
     *
     * @param container the parent view containing the washer layout
     */
    public WasherManager(View container) {
        this.container = container;

        // Initialize all washers to OPEN state by default
        for (int i = 1; i <= WASHER_IDS.length; i++) {
            washerStates.put(i, MachineStateHelper.STATE_OPEN);
        }
    }

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Updates the logical state and UI display for a specific washer.
     *
     * @param washerNum the machine number (1-based index)
     * @param state     the new state constant from {@link MachineStateHelper}
     */
    public void setState(int washerNum, int state) {
        washerStates.put(washerNum, state);
        updateUI(washerNum);
    }

    /**
     * Toggles the selection outline for a specific washer.
     *
     * @param washerNum the machine number (1-based index)
     * @param visible   true to show the blue outline, false to hide it
     */
    public void setOutline(int washerNum, boolean visible) {
        if (washerNum < 1 || washerNum > WASHER_IDS.length || container == null) return;

        View view = container.findViewById(WASHER_IDS[washerNum - 1]);
        MachineStateHelper.setOutlineVisible(view, visible);
    }

    /**
     * Refreshes the UI for all washers managed by this instance.
     */
    public void updateAll() {
        for (int i = 1; i <= WASHER_IDS.length; i++) {
            updateUI(i);
        }
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Syncs the UI of a specific washer with its logical state.
     *
     * @param washerNum the machine number to update
     */
    private void updateUI(int washerNum) {
        if (washerNum < 1 || washerNum > WASHER_IDS.length || container == null) return;

        // Ensure we only find views within this manager's specific container
        View view = container.findViewById(WASHER_IDS[washerNum - 1]);

        if (view != null) {
            MachineStateHelper.setMachineState(view, washerNum, washerStates.get(washerNum));
        }
    }
}