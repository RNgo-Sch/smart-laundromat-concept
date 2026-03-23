package com.example.smart_laundromat_concept.ui.activities.main.home;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.AppMachine;
import com.example.smart_laundromat_concept.data.remote.SupabaseClient;
import com.example.smart_laundromat_concept.data.session.UserSession;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Drives the smart status card on HomeActivity.
 * <p>
 * Shows one of two states depending on the user's session:
 * <ul>
 *   <li>Snapshot view — machine availability counts fetched from Supabase.</li>
 *   <li>Active booking view — live countdown timer for the user's running cycle.</li>
 * </ul>
 * <p>
 * The toggle button is only visible when the user has an active booking,
 * allowing them to switch between the countdown and the snapshot view.
 * <p>
 * <b>Usage:</b>
 * <pre>
 *   homeCardHelper = new HomeCardHelper(this); // in onCreate
 *   homeCardHelper.refresh();                  // in onResume
 *   homeCardHelper.stopTimer();                // in onDestroy
 * </pre>
 */
public class HomeCardHelper {

    // Store number used when querying Supabase
    private static final int STORE_NUMBER = 1;

    private final Activity activity;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a HomeCardHelper tied to a specific Activity.
     * Sets up the toggle button click listener.
     *
     * @param activity the host Activity containing the status card views
     */
    public HomeCardHelper(Activity activity) {
        this.activity = activity;
        setupToggle();
    }

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Re-evaluates the session state and shows the correct card.
     * Call from {@code onResume()} so it always reflects the latest state.
     */
    public void refresh() {
        UserSession session    = UserSession.getInstance();
        View snapshotView      = activity.findViewById(R.id.home_card__snapshot);
        View activeBookingView = activity.findViewById(R.id.home_card__active_booking);
        View toggleBtn         = activity.findViewById(R.id.home_card__toggle_button);

        if (snapshotView == null || activeBookingView == null) return;

        if (session.hasActiveBooking()) {
            // Show active booking countdown and reveal toggle button
            snapshotView.setVisibility(View.GONE);
            activeBookingView.setVisibility(View.VISIBLE);
            if (toggleBtn != null) toggleBtn.setVisibility(View.VISIBLE);
            stopTimer();
            bindActiveBooking(session);
        } else {
            // Show machine snapshot and hide toggle — nothing to toggle to
            activeBookingView.setVisibility(View.GONE);
            snapshotView.setVisibility(View.VISIBLE);
            if (toggleBtn != null) toggleBtn.setVisibility(View.GONE);
            stopTimer();
            loadMachineSnapshot();
        }
    }

    /**
     * Stops the countdown timer and removes all pending callbacks.
     * Must be called from {@code onDestroy()} to prevent memory leaks.
     */
    public void stopTimer() {
        if (timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
            timerRunnable = null;
        }
    }

    // -------------------------------------------------------------------------
    // Active Booking
    // -------------------------------------------------------------------------

    /**
     * Binds the active booking data from the session to the countdown card.
     *
     * @param session the current user session containing booking details
     */
    private void bindActiveBooking(UserSession session) {
        TextView machineLabel = activity.findViewById(R.id.active__machine_label);
        TextView statusLabel  = activity.findViewById(R.id.active__status_label);

        if (machineLabel != null) {
            // e.g. "Washer 2" or "Dryer 3"
            machineLabel.setText(session.getActiveMachineType() + " " + session.getActiveMachineNum());
        }

        if (statusLabel != null) {
            statusLabel.setText("In Progress");
        }

        startCountdown(session.getBookingEndTimeMillis());
    }

    /**
     * Starts a 1-second interval countdown timer that updates the timer TextView.
     * Automatically flips back to the snapshot view when the countdown reaches zero.
     *
     * @param endTimeMillis the booking end time in milliseconds
     */
    private void startCountdown(long endTimeMillis) {
        TextView timerView = activity.findViewById(R.id.active__timer);
        if (timerView == null) return;

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long remaining = endTimeMillis - System.currentTimeMillis();

                if (remaining <= 0) {
                    timerView.setText("Done — collect your laundry!");
                    UserSession.getInstance().clearActiveBooking();

                    // Flip back to snapshot view after a short delay
                    timerHandler.postDelayed(() -> refresh(), 2000);
                    return;
                }

                long minutes = (remaining / 1000) / 60;
                long seconds = (remaining / 1000) % 60;
                timerView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 1000);
            }
        };

        timerHandler.post(timerRunnable);
    }

    // -------------------------------------------------------------------------
    // Machine Snapshot
    // -------------------------------------------------------------------------

    /**
     * Loads machine availability counts from Supabase for all four status TextViews.
     */
    private void loadMachineSnapshot() {
        loadCountForStatus("washer", "open",   R.id.snapshot__washer_available);
        loadCountForStatus("washer", "in_use", R.id.snapshot__washer_in_use);
        loadCountForStatus("dryer",  "open",   R.id.snapshot__dryer_available);
        loadCountForStatus("dryer",  "in_use", R.id.snapshot__dryer_in_use);
    }

    /**
     * Queries Supabase for a machine count by type and status, then updates the given TextView.
     *
     * @param type   "washer" or "dryer"
     * @param status e.g. "open", "in_use", "reserved"
     * @param viewId the TextView resource ID to update with the count
     */
    private void loadCountForStatus(String type, String status, int viewId) {
        // Supabase PostgREST expects "eq.<value>" for equality filters
        String statusQuery = "eq." + status;

        SupabaseClient.getApi()
                .getMachineByStoreTypeStatus(STORE_NUMBER, type, statusQuery)
                .enqueue(new Callback<List<AppMachine>>() {
                    @Override
                    public void onResponse(Call<List<AppMachine>> call, Response<List<AppMachine>> response) {
                        int count = 0;
                        if (response.isSuccessful() && response.body() != null) {
                            count = response.body().size();
                        }

                        final int finalCount = count;
                        activity.runOnUiThread(() -> {
                            TextView tv = activity.findViewById(viewId);
                            if (tv != null) tv.setText(String.valueOf(finalCount));
                        });
                    }

                    @Override
                    public void onFailure(Call<List<AppMachine>> call, Throwable t) {
                        // Silently fail — snapshot will show dashes from XML default
                    }
                });
    }

    // -------------------------------------------------------------------------
    // Toggle Setup
    // -------------------------------------------------------------------------

    /**
     * Sets up the toggle button to switch between the snapshot and active booking views.
     * The toggle is only functional when the user has an active booking.
     */
    private void setupToggle() {
        View toggleBtn     = activity.findViewById(R.id.home_card__toggle_button);
        View snapshot      = activity.findViewById(R.id.home_card__snapshot);
        View activeBooking = activity.findViewById(R.id.home_card__active_booking);

        if (toggleBtn == null || snapshot == null || activeBooking == null) return;

        toggleBtn.setOnClickListener(v -> {
            // Only allow toggle when user has an active booking
            if (!UserSession.getInstance().hasActiveBooking()) return;

            boolean isSnapshotVisible = snapshot.getVisibility() == View.VISIBLE;
            snapshot.setVisibility(isSnapshotVisible ? View.GONE : View.VISIBLE);
            activeBooking.setVisibility(isSnapshotVisible ? View.VISIBLE : View.GONE);
        });
    }
}