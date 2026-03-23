package com.example.smart_laundromat_concept.ui.activities.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

/**
 * ProfileActivity handles the user profile screen.
 * It displays account details, balance, and user settings.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference
 * (e.g., {@link MenuBarHelper#menuBar}) to jump directly to its implementation.
 */
public class ProfileActivity extends AppCompatActivity {

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Initializes the Activity and sets up the static layout elements.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup full-screen display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Adjust padding to avoid UI elements being hidden behind system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_profile__root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Highlight the 'Profile' icon in the bottom menu bar
        MenuBarHelper.menuBar(this, MenuBarHelper.PROFILE);
    }

    /**
     * Called every time the Activity becomes visible.
     * Ensures the username and avatar are always up-to-date with the current session.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    // -------------------------------------------------------------------------
    // UI Data
    // -------------------------------------------------------------------------

    /**
     * Retrieves the username from the session and updates the username and avatar UI.
     */
    private void loadUserData() {
        if (!UserSession.getInstance().isLoggedIn()) return;

        String username = UserSession.getInstance().getUsername();

        TextView tvUsernameTitle = findViewById(R.id.activity_profile__username_title);
        TextView tvLetterCircle = findViewById(R.id.activity_profile__letter_circle);

        if (tvUsernameTitle != null) {
            tvUsernameTitle.setText(username);
        }

        if (tvLetterCircle != null) {
            tvLetterCircle.setText(username.substring(0, 1).toUpperCase());
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