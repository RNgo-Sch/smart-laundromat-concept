package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.view.View;

import com.example.smart_laundromat_concept.R;

/**
 * Manages the UI state for the washer machine section in {@link BookingActivity}.
 *
 * <p><b>OOP Design — Inheritance:</b><br>
 * All shared logic (state tracking, outline toggling, UI refresh) lives in the
 * parent class {@link MachineManager}. This class only provides the washer-specific
 * view IDs via {@link #getMachineIds()}, keeping it concise and focused.
 *
 * @see MachineManager
 * @see DryerManager
 */
public class WasherManager extends MachineManager {

    // Resource IDs for the four washer machine card views in the layout
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
     * Constructs a WasherManager for the given washer container view.
     *
     * @param container the parent view containing all washer card views
     */
    public WasherManager(View container) {
        super(container);
    }

    // -------------------------------------------------------------------------
    // MachineManager
    // -------------------------------------------------------------------------

    /**
     * Provides the washer-specific view IDs to the base class.
     * Index 0 corresponds to Washer 1, index 1 to Washer 2, and so on.
     *
     * @return array of washer card view resource IDs
     */
    @Override
    protected int[] getMachineIds() {
        return WASHER_IDS;
    }
}