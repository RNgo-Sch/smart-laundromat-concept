package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.smart_laundromat_concept.R;

/**
 * MachineStateHelper provides centralized logic for updating laundry machine UI components.
 *
 * This class handles:
 * - Toggling selection outlines via view activation states.
 * - Dynamically resolving resource IDs for machine labels and status indicators.
 * - Mapping state constants to localized strings and theme-consistent colors.
 *
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any state constant
 * (e.g., {@link #STATE_IN_USE}) to see where it is referenced.
 */
public class MachineStateHelper {

    /** Machine is available for immediate use. */
    public static final int STATE_OPEN = 1;
    /** Laundry is finished and waiting for collection. */
    public static final int STATE_COLLECTION = 2;
    /** Machine has been pre-booked by a user. */
    public static final int STATE_RESERVED = 3;
    /** Machine is currently running a cycle. */
    public static final int STATE_IN_USE = 4;
    /** Machine is broken or under maintenance. */
    public static final int STATE_OUT_OF_SERVICE = 5;

    /**
     * Toggles the selection outline (foreground) of a machine view.
     * Uses the 'activated' state which is bound to shape_blue_outline.xml.
     *
     * @param view   The machine view to update.
     * @param active True to show the outline, false to hide it.
     */
    public static void setOutlineVisible(View view, boolean active) {
        if (view != null) {
            view.setActivated(active);
        }
    }

    /**
     * Sets the state of a machine button, updating its text, text color, and background tint.
     *
     * This method uses dynamic resource lookup (getIdentifier) to find sub-views,
     * allowing it to work with any layout following the "machine_number_X" naming convention.
     *
     * @param view       The machine's root container (usually a LinearLayout).
     * @param machineNum The machine number (used to find specific TextViews).
     * @param state      The state constant (e.g., STATE_OPEN, STATE_IN_USE).
     */
    public static void setMachineState(View view, int machineNum, int state) {
        if (view == null) return;

        // Dynamically resolve IDs based on the machine index to avoid hardcoding
        String numIdStr = "machine_number_" + machineNum;
        String statusIdStr = "machine_status_" + machineNum;
        
        int numId = view.getContext().getResources().getIdentifier(numIdStr, "id", view.getContext().getPackageName());
        int statusId = view.getContext().getResources().getIdentifier(statusIdStr, "id", view.getContext().getPackageName());

        TextView tvNumber = view.findViewById(numId);
        TextView tvStatus = view.findViewById(statusId);

        if (tvNumber != null) {
            tvNumber.setText(String.valueOf(machineNum));
        }

        if (tvStatus != null) {
            int bgColorRes;
            String statusText;
            int statusColorRes = R.color.blue; // Default status color (Standard Blue)

            switch (state) {
                case STATE_COLLECTION:
                    statusText = view.getContext().getString(R.string.status_collection);
                    bgColorRes = R.color.green_50_visibility;
                    break;
                case STATE_RESERVED:
                    statusText = view.getContext().getString(R.string.status_reserved);
                    bgColorRes = R.color.orange_50_visibility;
                    break;
                case STATE_IN_USE:
                    statusText = view.getContext().getString(R.string.status_in_use);
                    bgColorRes = R.color.red_50_visibility;
                    break;
                case STATE_OUT_OF_SERVICE:
                    statusText = view.getContext().getString(R.string.status_out_of_service);
                    bgColorRes = R.color.grey_50_visibility;
                    break;
                case STATE_OPEN:
                default:
                    statusText = view.getContext().getString(R.string.status_open);
                    bgColorRes = R.color.green_50_visibility;
                    break;

            }

            // Apply updates to the UI components
            tvStatus.setText(statusText);
            tvStatus.setTextColor(ContextCompat.getColor(view.getContext(), statusColorRes));
            view.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), bgColorRes)));
        }
    }
}
