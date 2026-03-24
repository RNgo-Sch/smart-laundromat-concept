package com.example.smart_laundromat_concept.ui.activities.main.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.User;
import com.example.smart_laundromat_concept.data.remote.UserRepository;
import com.example.smart_laundromat_concept.data.session.LocationSession;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * HomeActivity is the main landing page after a user logs in.
 * It displays the primary dashboard of the application.
 * <p>
 * The smart status card switches automatically between:
 * <ul>
 *   <li>Machine availability snapshot (when idle)</li>
 *   <li>Live countdown timer (when user has an active booking)</li>
 * </ul>
 */
public class HomeActivity extends AppCompatActivity {

    private HomeCardHelper homeCardHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.activity_home__root), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);
                    return insets;
                });

        MenuBarHelper.menuBar(this, MenuBarHelper.HOME);
        LocationHelper.setupUnderline(this);
        homeCardHelper = new HomeCardHelper(this);

        swipeRefreshLayout = findViewById(R.id.activity_home__swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::refreshAll);
    }

    public void onTopUpClick(View view) {
        UserSession session = UserSession.getInstance();
        float amount = 250;
        float oldBalance = session.getWallet();
        float newBalance = oldBalance + amount;

        // 1. Update local state
        session.topUp(amount);

        // 2. Sync with backend
        UserRepository.updateBalance(
                session.getCurrentUser().getId(),
                newBalance,
                new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            loadUserData();
                        } else {
                            // rollback if failed
                            session.getCurrentUser().setWallet(oldBalance);
                            Toast.makeText(HomeActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        session.getCurrentUser().setWallet(oldBalance);
                        Toast.makeText(HomeActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeCardHelper.refresh();
        loadUserData();
        updateLocationName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeCardHelper.stopTimer();
    }

    /**
     * Refreshes all data on the home screen, including syncing with Supabase.
     */
    private void refreshAll() {
        UserSession session = UserSession.getInstance();
        if (!session.isLoggedIn()) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        // Fetch latest data from Supabase
        UserRepository.fetchUserByUsername(session.getUsername(), new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Update local session with fresh data from server
                    User freshUser = response.body().get(0);
                    session.setCurrentUser(freshUser);
                    
                    // Update UI
                    loadUserData();
                    homeCardHelper.refresh();
                    updateLocationName();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to sync data", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadUserData() {
        UserSession session = UserSession.getInstance();

        TextView walletBalance = findViewById(R.id.activity_home__wallet_balance);
        if (walletBalance != null) {
            walletBalance.setText(String.format("%.2f", session.getWallet()));
        }

        TextView reputationTitle = findViewById(R.id.reputation__tier_title);
        if (reputationTitle != null) {
            int tier = getReputationTier(session.getReputation());
            reputationTitle.setText("Reputation: Tier " + tier);
        }

        ProgressBar reputationBar   = findViewById(R.id.reputation__progress_bar);
        TextView reputationProgress = findViewById(R.id.reputation__progress_text);
        if (reputationBar != null && reputationProgress != null) {
            int score = session.getReputation();
            reputationBar.setProgress(score);
            reputationProgress.setText(score + "/100");
        }

        TextView refreshDate = findViewById(R.id.reputation__refresh_date);
        if (refreshDate != null) {
            refreshDate.setText(getLastRefreshedLabel());
        }
    }

    private void updateLocationName() {
        TextView locationName = findViewById(R.id.location_name);
        if (locationName != null) {
            locationName.setText(LocationSession.getInstance().getLocationName());
        }
    }

    private int getReputationTier(int score) {
        if (score >= 100) return 4;
        if (score >= 50)  return 3;
        if (score >= 25)  return 2;
        return 1;
    }

    private String getLastRefreshedLabel() {
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

    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}
