package com.example.smart_laundromat_concept.ui.activities.main.home;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.AppMachine;
import com.example.smart_laundromat_concept.data.session.UserSession;

import java.util.Locale;

public class HomeCardHelper {

    private final Activity activity;
    private boolean userChoseSnapshotView = false;

    public HomeCardHelper(Activity activity) {
        this.activity = activity;
        setupToggle();
    }

    public void refresh() {
        UserSession session    = UserSession.getInstance();
        View snapshotView      = activity.findViewById(R.id.home_card__snapshot);
        View activeBookingView = activity.findViewById(R.id.home_card__active_booking);
        View toggleBtn         = activity.findViewById(R.id.home_card__toggle_button);

        if (snapshotView == null || activeBookingView == null) return;

        boolean hasBooking = session.hasActiveBooking()
                || session.getBookingEndTimeMillis() > System.currentTimeMillis();

        if (toggleBtn != null) {
            toggleBtn.setVisibility(hasBooking ? View.VISIBLE : View.GONE);
        }

        if (!hasBooking) {
            activeBookingView.setVisibility(View.GONE);
            snapshotView.setVisibility(View.VISIBLE);
            return;
        }

        if (userChoseSnapshotView) {
            activeBookingView.setVisibility(View.GONE);
            snapshotView.setVisibility(View.VISIBLE);
        } else {
            snapshotView.setVisibility(View.GONE);
            activeBookingView.setVisibility(View.VISIBLE);
        }

        bindActiveBooking(session);
    }

    private void bindActiveBooking(UserSession session) {
        TextView machineLabel = activity.findViewById(R.id.active__machine_label);
        TextView statusLabel  = activity.findViewById(R.id.active__status_label);
        TextView timerView    = activity.findViewById(R.id.active__timer);

        if (machineLabel != null) {
            machineLabel.setText(session.getActiveMachineType() + " " + session.getActiveMachineNum());
        }

        if (statusLabel != null) {
            AppMachine.State state = session.getActiveMachineState();
            if (state != null) {
                statusLabel.setText(activity.getString(state.getStringResId()));
            }
        }

        if (timerView != null) {
            long remaining = session.getBookingEndTimeMillis() - System.currentTimeMillis();
            if (remaining > 0) {
                long minutes = (remaining / 1000) / 60;
                long seconds = (remaining / 1000) % 60;
                timerView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            } else {
                timerView.setText("Done");
            }
        }
    }

    private void setupToggle() {
        View toggleBtn     = activity.findViewById(R.id.home_card__toggle_button);
        View snapshot      = activity.findViewById(R.id.home_card__snapshot);
        View activeBooking = activity.findViewById(R.id.home_card__active_booking);

        if (toggleBtn == null || snapshot == null || activeBooking == null) return;

        toggleBtn.setOnClickListener(v -> {
            boolean isActiveVisible = activeBooking.getVisibility() == View.VISIBLE;
            userChoseSnapshotView = isActiveVisible;
            activeBooking.setVisibility(isActiveVisible ? View.GONE : View.VISIBLE);
            snapshot.setVisibility(isActiveVisible ? View.VISIBLE : View.GONE);
        });
    }
}