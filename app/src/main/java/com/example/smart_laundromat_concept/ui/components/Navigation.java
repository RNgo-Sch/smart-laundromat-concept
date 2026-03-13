package com.example.smart_laundromat_concept.ui.components;

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
 * Navigation class handles the routing between different activities in the application.
 * It centralizes navigation logic to avoid duplication across multiple Activity classes.
 */
public class Navigation {

    /**
     * Navigates to a new page based on the ID of the view (usually a button) that was clicked.
     *
     * @param activity The current activity context.
     * @param view     The view that triggered the navigation.
     */
    public static void launchPage(Activity activity, View view){

        Intent intent = null;
        int id = view.getId();

        //=======================================================================================
        // Authentication Flow: Handles buttons in activity_main.xml (used for Login and Sign Up)
        //=======================================================================================
        
        // Handle the main action button (Login or Create Account)
        if (id == R.id.activity_main__login_Button) {
            // Check if the current activity is MainActivity (the Login screen)
            if (activity instanceof MainActivity) {
                // If on Login page, clicking 'Login' leads to the Home screen
                intent = new Intent(activity, HomeActivity.class);

            } else if (activity instanceof SignUpActivity) {
                // If on Sign Up page, clicking 'Create Account' returns to the Login screen
                intent = new Intent(activity, MainActivity.class);
            }

        // Handle the switch button (Sign Up instead? or Return to Login?)
        } else if (id == R.id.activity_main__go_to_signup_Button) {
            if (activity instanceof MainActivity) {
                // Navigate from Login to Sign Up
                intent = new Intent(activity, SignUpActivity.class);

            } else if (activity instanceof SignUpActivity) {
                // Return from Sign Up to Login
                intent = new Intent(activity, MainActivity.class);
            }

        //=======================================================================================
        // Global Navigation: Handles interactions with the shared bottom menu bar
        //=======================================================================================
        
        } else if (id == R.id.menu_bar___LinearLayout_Home) {
            // Navigate to Home
            intent = new Intent(activity, HomeActivity.class);

        } else if (id == R.id.menu_bar___LinearLayout_Booking) {
            // Navigate to Booking
            intent = new Intent(activity, BookingActivity.class);

        } else if (id == R.id.menu_bar___LinearLayout_Profile) {
            // Navigate to Profile
            intent = new Intent(activity, ProfileActivity.class);
        }


        // Execute navigation if a valid intent was created
        if (intent != null) {
            // Safety check: Don't restart the activity if the user is already on that page
            /**
            1. activity.getClass().getName():
             Gets the full name of the current screen (e.g., "com.example.HomeActivity").

            2. intent.getComponent().getClassName():
             Gets the full name of the target screen you want to go to (e.g., "com.example.ProfileActivity").v
             */
            if (!activity.getClass().getName().equals(intent.getComponent().getClassName())) {
                activity.startActivity(intent);
                // Use 0, 0 to disable default Android sliding transitions for a snappier feel
                activity.overridePendingTransition(0, 0);
            }
        }
    }
}
