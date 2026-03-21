package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.activities.auth.LogInActivity;
import com.example.smart_laundromat_concept.ui.activities.location.LocationActivity;
import com.example.smart_laundromat_concept.ui.activities.notification.NotificationActivity;

/**
 * Handles navigation for global system features like Notifications, Location, and Logout.
 * <p>
 * This navigator manages:
 * <ul>
 *   <li>Direct screen navigation (Notifications, Location).</li>
 *   <li>Overlay visibility toggling for the logout confirmation dialog.</li>
 *   <li>Session termination and task stack clearing during logout.</li>
 *   <li>Global back button behavior.</li>
 * </ul>
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage}
 * to see how these requests are dispatched.
 */
public class SystemNavigator implements NavigatorModule {

    // -------------------------------------------------------------------------
    // NavigatorModule
    // -------------------------------------------------------------------------

    /**
     * Processes system-wide navigation requests based on view IDs.
     *
     * @param activity the current Activity context
     * @param id       the ID of the clicked View
     * @return a {@link NavigationRequest} if the ID is handled, otherwise null
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {

        // --- 1. Notification screen ---
        if (id == R.id.activity_notification__Notification_Button) {
            return new NavigationRequest(NotificationActivity.class, NavigationRequest.AnimationType.SLIDE_RIGHT);
        }

        // --- 2. Location screen ---
        if (id == R.id.activity_location__Location_Button) {
            return new NavigationRequest(LocationActivity.class, NavigationRequest.AnimationType.SLIDE_RIGHT);
        }

        // --- 3. Logout — show confirmation overlay ---
        if (id == R.id.activity_profile__Logout_Button) {
            View overlay = activity.findViewById(R.id.activity_profile__logout_confirmation_overlay);
            if (overlay != null) overlay.setVisibility(View.VISIBLE);
            return null;
        }

        // --- 4. Logout — cancel and hide overlay ---
        if (id == R.id.activity_profile__cancel_logout_action) {
            View overlay = activity.findViewById(R.id.activity_profile__logout_confirmation_overlay);
            if (overlay != null) overlay.setVisibility(View.GONE);
            return null;
        }

        // --- 5. Logout — confirm, clear session and back stack ---
        if (id == R.id.activity_profile__confirm_logout_action) {
            UserSession.getInstance().logout();
            Intent logoutIntent = new Intent(activity, LogInActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return new NavigationRequest(logoutIntent, NavigationRequest.AnimationType.FADE);
        }

        // --- 6. Global back button ---
        if (id == R.id.Back_Button) {
            activity.finish();
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return null;
        }

        return null;
    }
}