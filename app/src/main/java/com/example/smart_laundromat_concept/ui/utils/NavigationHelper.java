package com.example.smart_laundromat_concept.ui.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.BookingActivity;
import com.example.smart_laundromat_concept.ui.activities.HomeActivity;
import com.example.smart_laundromat_concept.ui.activities.LocationActivity;
import com.example.smart_laundromat_concept.ui.activities.MainActivity;
import com.example.smart_laundromat_concept.ui.activities.NotificationActivity;
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
     * Internal enumeration to categorize different navigation animation styles.
     */
    private enum AnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, FADE
    }


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
        
        // Default to standard slide animation
        AnimationType selectedAnim = AnimationType.SLIDE_RIGHT;


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
                selectedAnim = AnimationType.SLIDE_RIGHT;


            } else if (activity instanceof SignUpActivity) {
                // Toggle from Sign Up back to Login
                intent = new Intent(activity, MainActivity.class);
                selectedAnim = AnimationType.SLIDE_LEFT;
            }


        // --- Phase 2: Global Navigation (Handles interactions with the bottom menu bar) ---

        } else if (id == R.id.menu_bar___LinearLayout_Home) {
            intent = new Intent(activity, HomeActivity.class);
            selectedAnim = AnimationType.FADE;

        } else if (id == R.id.menu_bar___LinearLayout_Booking) {
            intent = new Intent(activity, BookingActivity.class);
            selectedAnim = AnimationType.FADE;

        } else if (id == R.id.menu_bar___LinearLayout_Profile) {
            intent = new Intent(activity, ProfileActivity.class);
            selectedAnim = AnimationType.FADE;


        // --- Phase 3: Profile Logout Flow (Handles overlay visibility and session termination) ---

        } else if (id == R.id.activity_profile__Logout_Button) {
            // Show the confirmation overlay
            View overlay = activity.findViewById(R.id.activity_profile__logout_confirmation_overlay);
            if (overlay != null) overlay.setVisibility(View.VISIBLE);

        } else if (id == R.id.activity_profile__cancel_logout_action) {
            // Hide the confirmation overlay
            View overlay = activity.findViewById(R.id.activity_profile__logout_confirmation_overlay);
            if (overlay != null) overlay.setVisibility(View.GONE);

        } else if (id == R.id.activity_profile__confirm_logout_action) {
            // Perform logout: Navigate to Login and clear the backstack
            intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        // --- Phase 4: Notification Flow (Handles notification-related interactions) ---

        } else if (id == R.id.activity_notification__Notification_Button) {
            intent = new Intent(activity, NotificationActivity.class);
            
        } else if (id == R.id.Back_Button) {
            // Closes the current activity to reveal the previous one in the stack
            activity.finish();
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


        // --- Phase 5: Location Flow (Handles location-related interactions) ---

        } else if (id == R.id.activity_location__Location_Button) {
            intent = new Intent(activity, LocationActivity.class);
        }



        // --- Phase 6: Intent Execution & Animation Switch ---

        if (intent != null) {
            // Optimization: Prevent reloading the activity if the user is already on the target page.
            if (!activity.getClass().getName().equals(intent.getComponent().getClassName())) {
                activity.startActivity(intent);

                // Use a switch to professionally handle the transition animation
                switch (selectedAnim) {
                    case FADE:
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case SLIDE_LEFT:
                        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    case SLIDE_RIGHT:
                    default:
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                }
            }
        }
    }
}
