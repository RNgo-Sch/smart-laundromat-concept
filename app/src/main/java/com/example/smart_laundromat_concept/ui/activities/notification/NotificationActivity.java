package com.example.smart_laundromat_concept.ui.activities.notification;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.Notification;
import com.example.smart_laundromat_concept.data.remote.repository.NotificationRepository;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * NotificationActivity displays a list of notifications for the logged-in user.
 * Notifications are fetched from Supabase and displayed in a RecyclerView.
 */
public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.notification__recycler_view);
        tvEmpty      = findViewById(R.id.notification__empty_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadNotifications();
    }

    // -------------------------------------------------------------------------
    // Data
    // -------------------------------------------------------------------------

    /**
     * Fetches notifications from Supabase and populates the RecyclerView.
     */
    private void loadNotifications() {
        NotificationRepository.getNotifications(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (isFinishing() || isDestroyed()) return;

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    showNotifications(response.body());
                } else {
                    showEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                showEmpty();
            }
        });
    }

    /**
     * Displays the notification list.
     *
     * @param notifications the list of notifications to display
     */
    private void showNotifications(List<Notification> notifications) {
        recyclerView.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        recyclerView.setAdapter(new NotificationAdapter(notifications));
    }

    /**
     * Shows the empty state when there are no notifications.
     */
    private void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     */
    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}