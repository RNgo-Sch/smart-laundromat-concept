package com.example.smart_laundromat_concept.ui.activities.auth;

import android.app.Activity;
import android.widget.Button;

import com.example.smart_laundromat_concept.R;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Utility for configuring authentication UI states (Login / Sign Up).
 * <p>
 * Updates button labels and text based on the current mode.
 * <p></p>
 * This class does not handle navigation or validation logic.
 */
public class AuthUIHelper {


    /** Login UI mode. */
    public static final int MODE_LOGIN = 0;


    /** Sign-up UI mode. */
    public static final int MODE_SIGNUP = 1;


    /**
     * Restricts valid authentication modes to {@link #MODE_LOGIN} and {@link #MODE_SIGNUP}.
     * <p>
     * Used as a lightweight alternative to enums for Android memory efficiency.
     */
    @IntDef({MODE_LOGIN, MODE_SIGNUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AuthMode {}


    /**
     * Applies UI state for authentication screens.
     *
     * @param activity host activity containing the views
     * @param mode     UI mode ({@link #MODE_LOGIN} or {@link #MODE_SIGNUP})
     */
    public static void setup(Activity activity, @AuthMode int mode) {

        Button actionButton = activity.findViewById(R.id.activity_main__login_Button);
        Button switchButton = activity.findViewById(R.id.activity_main__Switch_Button);

        if (actionButton == null || switchButton == null) return;

        if (mode == MODE_LOGIN) {
            actionButton.setText(R.string.auth_login);
            switchButton.setText(R.string.auth_switch_to_signup);

        } else if (mode == MODE_SIGNUP) {
            actionButton.setText(R.string.auth_signup);
            switchButton.setText(R.string.auth_switch_to_login);
        }
    }
}
