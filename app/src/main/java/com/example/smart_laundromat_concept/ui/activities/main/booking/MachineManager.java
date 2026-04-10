package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.view.View;

import com.example.smart_laundromat_concept.data.model.AppMachine;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for managing a section of machines (washers or dryers) in the UI.
 *
 * <p><b>OOP Design — Inheritance & Abstraction:</b><br>
 * {@link WasherManager} and {@link DryerManager} share identical logic for tracking
 * machine states and updating the UI. Rather than duplicating that logic, both classes
 * extend this base, which provides all the shared behaviour. Each subclass only needs
 * to supply the list of view IDs for its own machine section via {@link #getMachineIds()}.
 *
 * <p><b>Responsibilities:</b>
 * <ul>
 *   <li>Maintaining an in-memory map of machine number → current {@link AppMachine.State}.</li>
 *   <li>Updating individual machine UI elements via {@link MachineStateHelper}.</li>
 *   <li>Showing or hiding the selection outline on a specific machine.</li>
 * </ul>
 */
public abstract class MachineManager {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /** The parent view that contains all machine card views for this section. */
    protected final View container;

    /** Tracks the current state of each machine (key = 1-based machine number). */
    private final Map<Integer, AppMachine.State> machineStates = new HashMap<>();

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Initialises the manager and sets all machines to {@link AppMachine.State#AVAILABLE}.
     *
     * @param container the parent view containing this section's machine cards
     */
    public MachineManager(View container) {
        this.container = container;

        // Default all machines to AVAILABLE on startup
        for (int i = 1; i <= getMachineIds().length; i++) {
            machineStates.put(i, AppMachine.State.AVAILABLE);
        }
    }

    // -------------------------------------------------------------------------
    // Abstract Method — subclasses must supply their view ID array
    // -------------------------------------------------------------------------

    /**
     * Returns the array of layout resource IDs for this section's machine cards.
     * Subclasses must override this to point to their specific machine views.
     *
     * @return array of R.id values, one per machine (index 0 = machine 1)
     */
    protected abstract int[] getMachineIds();

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Updates the state of a specific machine and refreshes its UI card.
     *
     * @param machineNum 1-based machine number (1 to 4)
     * @param state      the new machine state to display
     */
    public void setState(int machineNum, AppMachine.State state) {
        machineStates.put(machineNum, state);
        updateUI(machineNum);
    }

    /**
     * Shows or hides the blue selection outline on a specific machine card.
     * Used to highlight the machine assigned to the current user.
     *
     * @param machineNum 1-based machine number (1 to 4)
     * @param visible    true to show the outline, false to hide it
     */
    public void setOutline(int machineNum, boolean visible) {
        if (!isValidMachineNum(machineNum)) return;

        View view = container.findViewById(getMachineIds()[machineNum - 1]);
        MachineStateHelper.setOutlineVisible(view, visible);
    }

    /**
     * Refreshes the UI card for every machine in this section.
     * Call this after switching tabs or after a bulk state update.
     */
    public void updateAll() {
        for (int i = 1; i <= getMachineIds().length; i++) {
            updateUI(i);
        }
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Syncs the UI card of a specific machine to its current logical state.
     *
     * @param machineNum 1-based machine number
     */
    private void updateUI(int machineNum) {
        if (!isValidMachineNum(machineNum)) return;

        View view = container.findViewById(getMachineIds()[machineNum - 1]);
        if (view != null) {
            MachineStateHelper.setMachineState(view, machineNum, machineStates.get(machineNum));
        }
    }

    /**
     * Returns true if the machine number is within the valid range (1 to array length).
     *
     * @param machineNum the machine number to check
     * @return true if valid
     */
    private boolean isValidMachineNum(int machineNum) {
        return machineNum >= 1 && machineNum <= getMachineIds().length && container != null;
    }
}