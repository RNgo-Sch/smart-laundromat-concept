package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.view.View;

import com.example.smart_laundromat_concept.R;

/**
 * Manages the UI state for the dryer machine section in {@link BookingActivity}.
 *
 * <p><b>OOP Design — Inheritance:</b><br>
 * All shared logic (state tracking, outline toggling, UI refresh) lives in the
 * parent class {@link MachineManager}. This class only provides the dryer-specific
 * view IDs via {@link #getMachineIds()}, keeping it concise and focused.
 *
 * @see MachineManager
 * @see WasherManager
 */
public class DryerManager extends MachineManager {

    // Resource IDs for the four dryer machine card views in the layout
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
     * Constructs a DryerManager for the given dryer container view.
     *
     * @param container the parent view containing all dryer card views
     */
    public DryerManager(View container) {
        super(container);
    }

    // -------------------------------------------------------------------------
    // MachineManager
    // -------------------------------------------------------------------------

    /**
     * Provides the dryer-specific view IDs to the base class.
     * Index 0 corresponds to Dryer 1, index 1 to Dryer 2, and so on.
     *
     * @return array of dryer card view resource IDs
     */
    @Override
    protected int[] getMachineIds() {
        return DRYER_IDS;
    }
}