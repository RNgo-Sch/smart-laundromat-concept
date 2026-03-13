package com.example.smart_laundromat_concept.ui.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.BookingActivity;
import com.example.smart_laundromat_concept.ui.activities.HomeActivity;
import com.example.smart_laundromat_concept.ui.activities.MainActivity;
import com.example.smart_laundromat_concept.ui.activities.ProfileActivity;
import com.example.smart_laundromat_concept.ui.activities.SignUpActivity;

/**
 * Centralized utility class for managing screen navigation across the application.
 * This class handles routing logic to ensure consistency and reduce duplication in Activity classes.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference
 * (e.g., {@link MainActivity}) to jump directly to its implementation.
 */
public class NavigationHelper {


    /**
     * Navigates to the appropriate Activity based on the ID of the clicked View.
     *
     * @param activity The current Activity context.
     * @param view     The View (usually a button or menu item) that triggered the navigation.
     */
    public static void launchPage(Activity activity, View view){
        // Initialize intent and identify which element was clicked
        Intent intent = null;
        int id = view.getId();


        // --- Phase 1: Authentication Flow (Handles Login and Sign Up screen transitions) ---
        
        // Logic for the main action button (Primary Button)
        if (id == R.id.activity_main__login_Button) {
            if (activity instanceof MainActivity) {
                // If on Login page, 'Login' leads to Home
                intent = new Intent(activity, HomeActivity.class);

            } else if (activity instanceof SignUpActivity) {
                // If on Sign Up page, 'Create Account' returns to Login/Main
                intent = new Intent(activity, MainActivity.class);
            }

        // Logic for the mode switch button (Secondary Button)
        } else if (id == R.id.activity_main__go_to_signup_Button) {
            if (activity instanceof MainActivity) {
                // Toggle from Login to Sign Up
                intent = new Intent(activity, SignUpActivity.class);

            } else if (activity instanceof SignUpActivity) {
                // Toggle from Sign Up back to Login
                intent = new Intent(activity, MainActivity.class);
            }


        // --- Phase 2: Global Navigation (Handles interactions with the bottom menu bar) ---

        } else if (id == R.id.menu_bar___LinearLayout_Home) {
            intent = new Intent(activity, HomeActivity.class);

        } else if (id == R.id.menu_bar___LinearLayout_Booking) {
            intent = new Intent(activity, BookingActivity.class);

        } else if (id == R.id.menu_bar___LinearLayout_Profile) {
            intent = new Intent(activity, ProfileActivity.class);
        }


        // --- Phase 3: Intent Execution ---

        if (intent != null) {
            // Optimization: Prevent reloading the activity if the user is already on the target page.
            // (Note: activity.getClass().getName() is compared against the target class name)
            if (!activity.getClass().getName().equals(intent.getComponent().getClassName())) {
                activity.startActivity(intent);

                // Use 0, 0 to disable default sliding transitions for a snappier, "Web-like" feel
                activity.overridePendingTransition(0, 0);
            }
        }
    }
}
