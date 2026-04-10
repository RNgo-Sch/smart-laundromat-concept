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
 * Helper class that manages the reputation card UI inside {@link HomeActivity}.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Displaying the user's current reputation tier title (e.g. "Reputation: Tier 3").</li>
 *   <li>Updating the progress bar and progress text (e.g. "65/100").</li>
 *   <li>Showing a human-readable "last refreshed" label.</li>
 * </ul>
 *
 * <p>Call {@link #refresh()} from {@code onResume()} so the card always reflects
 * the latest data in {@link UserSession}.
 */
public class ReputationHelper {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /** The Activity that contains the reputation card views. */
    private final Activity activity;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Creates a ReputationHelper tied to the given Activity.
     *
     * @param activity the host Activity containing the reputation card views
     */
    public ReputationHelper(Activity activity) {
        this.activity = activity;
    }

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Reads the current reputation score from the session and updates all
     * reputation card views.
     *
     * <p>Call this from {@code onResume()} to keep the card in sync with session data.
     */
    public void refresh() {
        int score = UserSession.getInstance().getReputation();
        updateTierTitle(score);
        updateProgressBar(score);
        updateRefreshLabel();
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Updates the tier title text view.
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
     * Updates the progress bar fill and the "score/100" label.
     *
     * @param score the user's current reputation score
     */
    private void updateProgressBar(int score) {
        ProgressBar progressBar = activity.findViewById(R.id.reputation__progress_bar);
        TextView progressText   = activity.findViewById(R.id.reputation__progress_text);

        if (progressBar != null) progressBar.setProgress(score);
        if (progressText != null) progressText.setText(score + "/100");
    }

    /**
     * Updates the "last refreshed" label below the progress bar.
     */
    private void updateRefreshLabel() {
        TextView refreshLabel = activity.findViewById(R.id.reputation__refresh_date);
        if (refreshLabel != null) {
            refreshLabel.setText(buildRefreshLabel());
        }
    }

    /**
     * Returns the reputation tier (1–4) for a given score.
     *
     * <ul>
     *   <li>0–24  → Tier 1</li>
     *   <li>25–49 → Tier 2</li>
     *   <li>50–99 → Tier 3</li>
     *   <li>100+  → Tier 4</li>
     * </ul>
     *
     * @param score the user's current score
     * @return tier number from 1 to 4
     */
    private int getTier(int score) {
        if (score >= 100) return 4;
        if (score >= 50)  return 3;
        if (score >= 25)  return 2;
        return 1;
    }

    /**
     * Builds a human-readable string describing when the reputation was last refreshed.
     *
     * <p>Examples:
     * <ul>
     *   <li>"Last refreshed today at 2:35 PM"</li>
     *   <li>"Last refreshed yesterday"</li>
     *   <li>"Last refreshed on 10.03.2026"</li>
     * </ul>
     *
     * <p><b>TODO:</b> Replace the dummy "now" timestamp with the actual refresh
     * timestamp stored in Supabase.
     *
     * @return formatted label string
     */
    private String buildRefreshLabel() {
        // TODO: Replace with actual last-refresh timestamp from Supabase
        Calendar lastRefresh = Calendar.getInstance();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

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