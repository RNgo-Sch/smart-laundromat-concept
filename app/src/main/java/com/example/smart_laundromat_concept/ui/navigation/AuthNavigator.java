package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.auth.LogInActivity;
import com.example.smart_laundromat_concept.ui.activities.auth.SignUpActivity;
import com.example.smart_laundromat_concept.ui.activities.main.home.HomeActivity;

/**
 * Handles navigation logic for the Authentication domain (Login and Sign Up).
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage}
 * to see how these requests are executed.
 */
public class AuthNavigator implements NavigatorModule {

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Resets the navigation state.
     * Should be called when returning to the login screen (e.g., after logout).
     */
    public static void resetNavigation() {
        // Reserved for future navigation state resets if needed
    }

    /**
     * Processes authentication-related navigation requests based on view IDs.
     *
     * @param activity the current Activity context
     * @param id       the ID of the clicked View
     * @return a {@link NavigationRequest} if the ID is handled, otherwise null
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {

        // --- 1. Successful login — clear back stack so user cannot go back to login ---
        if (id == R.id.activity_main__login_Button) {
            Intent intent = new Intent(activity, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return new NavigationRequest(intent, NavigationRequest.AnimationType.FADE);
        }

        // --- 2. Toggle between Login and Sign Up screens ---
        if (id == R.id.activity_main__Switch_Button) {
            if (activity instanceof LogInActivity) {
                return new NavigationRequest(SignUpActivity.class, NavigationRequest.AnimationType.SLIDE_RIGHT);
            } else {
                return new NavigationRequest(LogInActivity.class, NavigationRequest.AnimationType.SLIDE_LEFT);
            }
        }

        return null;
    }
}