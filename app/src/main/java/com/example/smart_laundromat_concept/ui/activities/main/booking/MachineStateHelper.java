package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.AppMachine;

/**
 * Utility class for applying visual state to a single machine card view.
 *
 * <p>Each machine card contains a number label and a status badge. This helper
 * sets both the badge text and the card's background tint colour based on the
 * machine's current {@link AppMachine.State}.
 *
 * <p>This class contains only static methods and holds no state of its own.
 * It is called by {@link MachineManager} whenever a machine's state changes.
 */
public class MachineStateHelper {

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Shows or hides the blue selection outline on a machine card.
     * The outline is shown when the card is in the "activated" state.
     *
     * @param view   the machine card view to update
     * @param active true to show the outline, false to hide it
     */
    public static void setOutlineVisible(View view, boolean active) {
        if (view != null) {
            view.setActivated(active);
        }
    }

    /**
     * Updates a machine card's number label, status badge text, and background colour
     * to reflect the given machine state.
     *
     * <p>Background tint colours per state:
     * <ul>
     *   <li>AVAILABLE / COLLECTION → green (50% opacity)</li>
     *   <li>RESERVED               → orange (50% opacity)</li>
     *   <li>IN_USE                 → red (50% opacity)</li>
     *   <li>OOS                    → grey (50% opacity)</li>
     * </ul>
     *
     * @param view       the machine card view containing the number and status TextViews
     * @param machineNum the 1-based machine number to display on the card
     * @param state      the current state of the machine
     */
    public static void setMachineState(View view, int machineNum, AppMachine.State state) {
        if (view == null) return;

        // Resolve TextViews using dynamically named IDs (machine_number_1, machine_status_1, etc.)
        int numId = view.getContext().getResources().getIdentifier(
                "machine_number_" + machineNum, "id", view.getContext().getPackageName());
        int statusId = view.getContext().getResources().getIdentifier(
                "machine_status_" + machineNum, "id", view.getContext().getPackageName());

        TextView tvNumber = view.findViewById(numId);
        TextView tvStatus = view.findViewById(statusId);

        // Set the machine number label
        if (tvNumber != null) {
            tvNumber.setText(String.valueOf(machineNum));
        }

        if (tvStatus != null) {
            // Choose background tint based on state
            int bgColorRes;
            switch (state) {
                case RESERVED:   bgColorRes = R.color.orange_50_visibility; break;
                case IN_USE:     bgColorRes = R.color.red_50_visibility;    break;
                case OOS:        bgColorRes = R.color.grey_50_visibility;   break;
                case COLLECTION: bgColorRes = R.color.purple_50_visibility;  break;
                case AVAILABLE:
                default:         bgColorRes = R.color.green_50_visibility;  break;
            }

            // Set badge text from the state's string resource
            tvStatus.setText(view.getContext().getString(state.getStringResId()));

            // Status text is always shown in blue
            tvStatus.setTextColor(ContextCompat.getColor(view.getContext(), R.color.blue));

            // Apply background tint to the card
            view.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(view.getContext(), bgColorRes)));
        }
    }
}