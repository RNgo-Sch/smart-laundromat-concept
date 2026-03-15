package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.auth.LogInActivity;
import com.example.smart_laundromat_concept.ui.activities.auth.SignUpActivity;
import com.example.smart_laundromat_concept.ui.activities.main.HomeActivity;

/**
 * Handles navigation logic for the Authentication domain.
 */
public class AuthNavigator {

    public static NavigationRequest handleAuth(Activity activity, int id) {
        // --- 1. Login Logic ---
        if (id == R.id.activity_main__login_Button) {
            Class<? extends Activity> target = (activity instanceof LogInActivity) ? HomeActivity.class : LogInActivity.class;
//            Class<? extends Activity> target;
//            if (activity instanceof LogInActivity) {
//                target = HomeActivity.class;
//            } else {
//                target = LogInActivity.class;
//            }
            return new NavigationRequest(target, NavigationRequest.AnimationType.FADE);
        }
        
        // --- 2. Switch Logic (Login <-> Sign Up) ---
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
