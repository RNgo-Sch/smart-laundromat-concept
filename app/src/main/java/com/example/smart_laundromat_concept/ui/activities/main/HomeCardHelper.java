package com.example.smart_laundromat_concept.ui.activities.main;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.Machine;
import com.example.smart_laundromat_concept.data.remote.SupabaseClient;
import com.example.smart_laundromat_concept.data.session.UserSession;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * HomeCardHelper drives the smart status card on HomeActivity.
 * <p>
 * It shows one of two states depending on the user's session:
 * <ul>
 *   <li><b>Snapshot view</b>  – machine availability counts fetched from Supabase.</li>
 *   <li><b>Active booking view</b> – live countdown timer for the user's running cycle.</li>
 * </ul>
 *
 * <b>Usage:</b>
 * <pre>
 *   // In HomeActivity
 *   homeCardHelper = new HomeCardHelper(this);
 *
 *   // onResume
 *   homeCardHelper.refresh();
 *
 *   // onDestroy
 *   homeCardHelper.stopTimer();
 * </pre>
 */
public class HomeCardHelper {

    private static final String TAG = "HomeCardHelper";

    // Store number used when querying Supabase — change to match your setup
    private static final int STORE_NUMBER = 1;

    private final Activity activity;
    private final Handler  timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;

    public HomeCardHelper(Activity activity) {
        this.activity = activity;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Re-evaluates the session state and shows the correct card.
     * Call from {@code onResume()} so it always reflects the latest state.
     */
    public void refresh() {
        UserSession session      = UserSession.getInstance();
        View snapshotView        = activity.findViewById(R.id.home_card__snapshot);
        View activeBookingView   = activity.findViewById(R.id.home_card__active_booking);

        if (snapshotView == null || activeBookingView == null) return;

        if (session.hasActiveBooking()) {
            // Show live countdown
            snapshotView.setVisibility(View.GONE);
            activeBookingView.setVisibility(View.VISIBLE);
            stopTimer();
            bindActiveBooking(session);
        } else {
            // Show availability snapshot
            activeBookingView.setVisibility(View.GONE);
            snapshotView.setVisibility(View.VISIBLE);
            stopTimer();
            loadMachineSnapshot();
        }
    }

    /**
     * Stops the countdown timer and removes pending callbacks.
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

    private void bindActiveBooking(UserSession session) {
        TextView machineLabel = activity.findViewById(R.id.active__machine_label);
        TextView statusLabel  = activity.findViewById(R.id.active__status_label);

        if (machineLabel != null) {
            machineLabel.setText(session.getActiveMachineType() + " " + session.getActiveMachineNum());
        }
        if (statusLabel != null) {
            statusLabel.setText("In Progress");
        }

        startCountdown(session.getBookingEndTimeMillis());
    }

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

    private void loadMachineSnapshot() {
        loadCountForStatus("washer", "open",         R.id.snapshot__washer_available);
        loadCountForStatus("washer", "in_use",       R.id.snapshot__washer_in_use);
        loadCountForStatus("dryer",  "open",         R.id.snapshot__dryer_available);
        loadCountForStatus("dryer",  "in_use",       R.id.snapshot__dryer_in_use);
    }

    /**
     * Queries Supabase for a machine count and updates the given TextView.
     *
     * @param type     "washer" or "dryer"
     * @param status   e.g. "open", "in_use", "reserved"
     * @param viewId   The TextView resource ID to update
     */
    private void loadCountForStatus(String type, String status, int viewId) {
        // Supabase PostgREST expects "eq.<value>" for equality filters
        String statusQuery = "eq." + status;

        SupabaseClient.getApi()
                .getMachineByStoreTypeStatus(STORE_NUMBER, type, statusQuery)
                .enqueue(new Callback<List<Machine>>() {
                    @Override
                    public void onResponse(Call<List<Machine>> call,
                                           Response<List<Machine>> response) {
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
                    public void onFailure(Call<List<Machine>> call, Throwable t) {
                        Log.e(TAG, "Failed to load count for " + type + "/" + status
                                + ": " + t.getMessage());
                    }
                });
    }
}
