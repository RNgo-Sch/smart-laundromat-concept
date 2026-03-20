package com.example.smart_laundromat_concept.ui.activities.main;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

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

    /**
     * Initializes the Activity, sets up the layout, and configures the menu bar.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup full-screen display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Highlight the 'Home' icon in the bottom menu bar
        MenuBarHelper.menuBar(this, MenuBarHelper.HOME);

        // Apply the underline effect to the 'Change Location' text
        LocationHelper.setupUnderline(this);

        // Standard edge-to-edge padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.activity_home__root), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);
                    return insets;
                });

        // Initialise the status card helper
        homeCardHelper = new HomeCardHelper(this);
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
    }

    /**
     * Stops the countdown timer to prevent memory leaks when the Activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeCardHelper.stopTimer();
    }

    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     * <p>
     * (Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage} to jump to the method)
     */
    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}
