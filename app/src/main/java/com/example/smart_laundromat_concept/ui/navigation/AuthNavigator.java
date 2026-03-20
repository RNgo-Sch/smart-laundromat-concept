package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.auth.LogInActivity;
import com.example.smart_laundromat_concept.ui.activities.auth.SignUpActivity;
import com.example.smart_laundromat_concept.ui.activities.main.HomeActivity;

/**
 * Handles navigation logic for the Authentication domain (Login and Sign Up).
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage}
 * to see how these requests are executed.
 */
public class AuthNavigator implements NavigatorModule {

    /**
     * Prevents multiple navigation events from firing simultaneously.
     */
    public static boolean hasNavigated = false;

    /**
     * Resets navigation guard.
     * Should be called when returning to login screen (e.g., logout).
     */
    public static void resetNavigation() {
        hasNavigated = false;
    }

    /**
     * Processes authentication-related navigation requests based on view IDs.
     *
     * @param activity The current Activity context.
     * @param id The ID of the clicked View.
     * @return A {@link NavigationRequest} if the ID is handled, otherwise null.
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {
        // --- 1. Login/Success Logic ---
        if (id == R.id.activity_main__login_Button) {

            android.util.Log.d("NAV_DEBUG", "Login navigation triggered: " + System.currentTimeMillis());

            Class<? extends Activity> target = HomeActivity.class;
            android.util.Log.d("NAV_DEBUG", "Navigating to HomeActivity");
            return new NavigationRequest(target, NavigationRequest.AnimationType.FADE);
        }

        // --- 2. Toggle Logic (Switching between Login and Sign Up screens) ---
        if (id == R.id.activity_main__go_to_signup_Button) {
            if (activity instanceof LogInActivity) {
                return new NavigationRequest(SignUpActivity.class, NavigationRequest.AnimationType.SLIDE_RIGHT);
            } else {
                return new NavigationRequest(LogInActivity.class, NavigationRequest.AnimationType.SLIDE_LEFT);
            }
        }
        return null;
    }
}
