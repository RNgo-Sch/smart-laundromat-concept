package com.example.smart_laundromat_concept.classes;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import com.example.smart_laundromat_concept.R;

/**
 * Utility class to handle switching between Login and Sign Up UI modes
 * for the shared activity_main layout.
 */
public class LogInSignUpMode {
    public static final int MODE_LOGIN = 0;
    public static final int MODE_SIGNUP = 1;

    /**
     * Configures the UI components of the provided activity based on the specified mode.
     *
     * @param activity The activity containing the views to be configured.
     * @param mode     The mode to set (MODE_LOGIN or MODE_SIGNUP).
     */
    public static void setup(Activity activity, int mode) {
        //TextView title = activity.findViewById(R.id.activity_main__loginSignup_Text);
        Button actionButton = activity.findViewById(R.id.activity_main__login_Button);
        Button switchButton = activity.findViewById(R.id.activity_main__go_to_signup_Button);

        if (mode == MODE_LOGIN) {
            // Configure UI for Login state
            //if (title != null) title.setText("Login");
            if (actionButton != null) actionButton.setText("Login");
            if (switchButton != null) switchButton.setText("Don't have an account? Sign Up");
        } else if (mode == MODE_SIGNUP) {
            // Configure UI for Sign Up state
            //if (title != null) title.setText("Sign Up");
            if (actionButton != null) actionButton.setText("Create Account");
            if (switchButton != null) switchButton.setText("Already have an account? Return to Login");
        }
    }

    /**
     * Mock login validation logic.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return true if credentials match the mock user, false otherwise.
     */
    public static boolean login(String username, String password){
        return username.equals("user") && password.equals("1234");
    }

    /**
     * Mock signup validation logic.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return true if the username is not already taken, false otherwise.
     */
    public static boolean signup(String username, String password){
        // Basic check: if username is "user", it already exists
        if (username.equals("user")) {
            return false;
        }
        // Check if the password is empty
        if (password.isEmpty()) {
            return false;
        }
        // save account to database later
        return true;
    }
}
