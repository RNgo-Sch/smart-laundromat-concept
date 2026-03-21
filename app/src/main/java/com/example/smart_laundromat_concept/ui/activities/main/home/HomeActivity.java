package com.example.smart_laundromat_concept.ui.activities.main.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.session.LocationSession;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * HomeActivity is the main landing page after a user logs in.
 * It displays the primary dashboard of the application.
 * <p>
 * The smart status card switches automatically between:
 * <ul>
 *   <li>Machine availability snapshot (when idle)</li>
 *   <li>Live countdown timer (when user has an active booking)</li>
 * </ul>
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference
 * (e.g., {@link MenuBarHelper#menuBar}) to jump directly to its implementation.
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * Drives the smart status card (snapshot ↔ active booking).
     * (Hold Cmd/Ctrl + Click on {@link HomeCardHelper#refresh} to jump to the logic)
     */
    private HomeCardHelper homeCardHelper;

    /** Handles the pull-to-refresh gesture on the home screen. */
    private SwipeRefreshLayout swipeRefreshLayout;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Initializes the Activity, sets up the layout, and configures the menu bar.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup full-screen display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Adjust padding to avoid UI elements being hidden behind system bars
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.activity_home__root), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);
                    return insets;
                });

        // Highlight the 'Home' icon in the bottom menu bar
        MenuBarHelper.menuBar(this, MenuBarHelper.HOME);

        // Apply the underline effect to the 'Change Location' text
        LocationHelper.setupUnderline(this);

        // Initialize the status card helper
        homeCardHelper = new HomeCardHelper(this);

        // Initialize swipe to refresh
        swipeRefreshLayout = findViewById(R.id.activity_home__swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> refreshAll());
    }

    /**
     * Called every time the screen becomes visible.
     * Re-evaluates the session so the card always shows the correct state,
     * whether the user is coming from Booking, Profile, or reopening the app.
     */
    @Override
    protected void onResume() {
        super.onResume();
        homeCardHelper.refresh();
        loadUserData();
        updateLocationName();
    }

    /**
     * Stops the countdown timer to prevent memory leaks when the Activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeCardHelper.stopTimer();
    }

    // -------------------------------------------------------------------------
    // UI Data
    // -------------------------------------------------------------------------

    /**
     * Refreshes all data on the home screen.
     * Called when the user pulls down to refresh.
     */
    private void refreshAll() {
        homeCardHelper.refresh();
        loadUserData();
        updateLocationName();

        // Stop the spinning indicator after refresh
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Loads user data from the session and updates the wallet and reputation UI.
     */
    private void loadUserData() {
        UserSession session = UserSession.getInstance();

        // Update wallet balance
        TextView walletBalance = findViewById(R.id.activity_home__wallet_balance);
        if (walletBalance != null) {
            walletBalance.setText(String.format("%.2f", session.getWallet()));
        }

        // Update reputation tier title
        TextView reputationTitle = findViewById(R.id.reputation__tier_title);
        if (reputationTitle != null) {
            int tier = getReputationTier(session.getReputation());
            reputationTitle.setText("Reputation: Tier " + tier);
        }

        // Update reputation progress bar and text
        ProgressBar reputationBar   = findViewById(R.id.reputation__progress_bar);
        TextView reputationProgress = findViewById(R.id.reputation__progress_text);
        if (reputationBar != null && reputationProgress != null) {
            int score = session.getReputation();
            reputationBar.setProgress(score);
            reputationProgress.setText(score + "/100");
        }

        // Update last refreshed label
        TextView refreshDate = findViewById(R.id.reputation__refresh_date);
        if (refreshDate != null) {
            refreshDate.setText(getLastRefreshedLabel());
        }
    }

    /**
     * Updates the location name in the floating location bar.
     * Called in onResume so it refreshes every time the user returns from LocationActivity.
     */
    private void updateLocationName() {
        TextView locationName = findViewById(R.id.location_name);
        if (locationName != null) {
            locationName.setText(LocationSession.getInstance().getLocationName());
        }
    }

    /**
     * Returns the reputation tier (1-4) based on the user's score.
     *
     * @param score the user's current reputation score
     * @return tier number between 1 and 4
     */
    private int getReputationTier(int score) {
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

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     * <p>
     * (Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage} to jump to the method)
     */
    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}