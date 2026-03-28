package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.AppMachine;

public class MachineStateHelper {

    public static void setOutlineVisible(View view, boolean active) {
        if (view != null) {
            view.setActivated(active);
        }
    }

    public static void setMachineState(View view, int machineNum, AppMachine.State state) {
        if (view == null) return;

        int numId = view.getContext().getResources().getIdentifier(
                "machine_number_" + machineNum, "id", view.getContext().getPackageName());

        int statusId = view.getContext().getResources().getIdentifier(
                "machine_status_" + machineNum, "id", view.getContext().getPackageName());

        TextView tvNumber = view.findViewById(numId);
        TextView tvStatus = view.findViewById(statusId);

        if (tvNumber != null) {
            tvNumber.setText(String.valueOf(machineNum));
        }

        if (tvStatus != null) {
            int bgColorRes;
            int statusColorRes = R.color.blue;

            switch (state) {
                case RESERVED:
                    bgColorRes = R.color.orange_50_visibility;
                    break;
                case IN_USE:
                    bgColorRes = R.color.red_50_visibility;
                    break;
                case OOS:
                    bgColorRes = R.color.grey_50_visibility;
                    break;
                case AVAILABLE:
                case COLLECTION:
                default:
                    bgColorRes = R.color.green_50_visibility;
                    break;
            }


            String statusText = view.getContext().getString(state.getStringResId());

            tvStatus.setText(statusText);
            tvStatus.setTextColor(ContextCompat.getColor(view.getContext(), statusColorRes));

            view.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(view.getContext(), bgColorRes)));
        }
    }
}