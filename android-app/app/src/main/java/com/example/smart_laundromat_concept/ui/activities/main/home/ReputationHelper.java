package com.example.smart_laundromat_concept.ui.activities.main.home;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.session.UserSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Helper class for managing the reputation card UI on HomeActivity.
 * <p>
 * Responsibilities:
 * - Displaying the user's current reputation tier and score.
 * - Updating the progress bar to reflect the current score.
 * - Showing a human-readable last refreshed label.
 */
public class ReputationHelper {

    private final Activity activity;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a ReputationHelper tied to a specific Activity.
     *
     * @param activity the host Activity containing the reputation views
     */
    public ReputationHelper(Activity activity) {
        this.activity = activity;
    }

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Reads reputation data from the session and updates all reputation UI components.
     * Call from onResume() so it always reflects the latest session state.
     */
    public void refresh() {
        UserSession session = UserSession.getInstance();
        int score = session.getReputation();

        updateTierTitle(score);
        updateProgressBar(score);
        updateRefreshLabel();
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Updates the reputation tier title TextView.
     *
     * @param score the user's current reputation score
     */
    private void updateTierTitle(int score) {
        TextView tierTitle = activity.findViewById(R.id.reputation__tier_title);
        if (tierTitle != null) {
            tierTitle.setText("Reputation: Tier " + getTier(score));
        }
    }

    /**
     * Updates the progress bar and progress text TextView.
     *
     * @param score the user's current reputation score
     */
    private void updateProgressBar(int score) {
        ProgressBar progressBar = activity.findViewById(R.id.reputation__progress_bar);
        TextView progressText = activity.findViewById(R.id.reputation__progress_text);

        if (progressBar != null) {
            progressBar.setProgress(score);
        }
        if (progressText != null) {
            progressText.setText(score + "/100");
        }
    }

    /**
     * Updates the last refreshed label with a human-readable timestamp.
     */
    private void updateRefreshLabel() {
        TextView refreshLabel = activity.findViewById(R.id.reputation__refresh_date);
        if (refreshLabel != null) {
            refreshLabel.setText(getLastRefreshedLabel());
        }
    }

    /**
     * Returns the reputation tier (1-4) based on the user's score.
     *
     * @param score the user's current reputation score
     * @return tier number between 1 and 4
     */
    private int getTier(int score) {
        if (score >= 100) return 4;
        if (score >= 50)  return 3;
        if (score >= 25)  return 2;
        return 1;
    }

    /**
     * Returns a human-readable label showing when reputation was last refreshed.
     * <p>
     * Examples:
     * - "Last refreshed today at 2:35 PM"
     * - "Last refreshed yesterday"
     * - "Last refreshed on 10.03.2026"
     *
     * @return formatted label string
     */
    private String getLastRefreshedLabel() {
        // TODO: Replace with actual last refresh timestamp from Supabase
        Calendar lastRefresh = Calendar.getInstance();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = (Calendar) today.clone();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        if (lastRefresh.after(today)) {
            String time = new SimpleDateFormat("h:mm a", Locale.getDefault())
                    .format(lastRefresh.getTime());
            return "Last refreshed today at " + time;

        } else if (lastRefresh.after(yesterday)) {
            return "Last refreshed yesterday";

        } else {
            String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(lastRefresh.getTime());
            return "Last refreshed on " + date;
        }
    }
}