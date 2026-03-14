package com.example.smart_laundromat_concept.ui.utils;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smart_laundromat_concept.R;

/**
 * LoginToggleHelper handles the switching between Login and Sign Up UI modes.
 * This class is responsible only for visual state changes on the authentication screens.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link LoginToggleHelper#setup}) to jump directly to its implementation.
 */
public class LoginToggleHelper {


    /** Constant representing the Login state of the UI. */
    public static final int MODE_LOGIN = 0;

    /** Constant representing the Sign Up state of the UI. */
    public static final int MODE_SIGNUP = 1;


    /**
     * Configures the UI components of the provided activity based on the specified mode.
     * 
     * @param activity The Activity context containing the views to be modified.
     * @param mode     The target UI mode (MODE_LOGIN or MODE_SIGNUP).
     */
    public static void setup(Activity activity, int mode) {
        // Retrieve references to the main action and toggle buttons
        TextView title = activity.findViewById(R.id.activity_main__title_text);
        Button actionButton = activity.findViewById(R.id.activity_main__login_Button);
        Button switchButton = activity.findViewById(R.id.activity_main__go_to_signup_Button);


        // Apply text changes based on the requested mode
        if (mode == MODE_LOGIN) {
            // Configure UI for Login Mode
            // if (title != null) title.setText("Login");
            if (actionButton != null) actionButton.setText("Login");
            if (switchButton != null) switchButton.setText("Don't have an account? Sign Up");

        } else if (mode == MODE_SIGNUP) {
            // Configure UI for Sign Up Mode
            // if (title != null) title.setText("Sign Up");
            if (actionButton != null) actionButton.setText("Create Account");
            if (switchButton != null) switchButton.setText("Already have an account? Return to Login");
        }
    }
}
