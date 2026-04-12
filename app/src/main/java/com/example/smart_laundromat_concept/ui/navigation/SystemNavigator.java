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
 * Handles navigation for global system features: Notifications, Location,
 * Logout, the Reputation overlay, and the back button.
 *
 * <p>Some actions in this class do not produce a {@link NavigationRequest} — instead
 * they perform the action directly (e.g. showing/hiding an overlay) and return null
 * so {@link NavigationHelper} knows no further action is needed.
 */
public class SystemNavigator implements NavigatorModule {

    /**
     * Handles system-level navigation and overlay events.
     *
     * @param activity the current Activity context
     * @param id       the resource ID of the clicked view
     * @return a {@link NavigationRequest} if a screen change is needed, otherwise null
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {

        // --- Notifications ---
        if (id == R.id.activity_notification__Notification_Button) {
            return new NavigationRequest(NotificationActivity.class,
                    NavigationRequest.AnimationType.SLIDE_RIGHT);
        }

        // --- Location ---
        if (id == R.id.activity_location__Location_Button) {
            return new NavigationRequest(LocationActivity.class,
                    NavigationRequest.AnimationType.SLIDE_RIGHT);
        }

        // --- Logout: show confirmation overlay ---
        if (id == R.id.activity_profile__Logout_Button) {
            showOverlay(activity, R.id.activity_profile__logout_confirmation_overlay);
            return null;
        }

        // --- Logout: cancel (hide overlay) ---
        if (id == R.id.activity_profile__cancel_logout_action) {
            hideOverlay(activity, R.id.activity_profile__logout_confirmation_overlay);
            return null;
        }

        // --- Logout: confirm — clear session and navigate to Login ---
        if (id == R.id.activity_profile__confirm_logout_action) {
            UserSession.getInstance().logout();
            Intent intent = new Intent(activity, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return new NavigationRequest(intent, NavigationRequest.AnimationType.FADE);
        }

        // --- Reputation overlay: open ---
        if (id == R.id.reputation__progress_details || id == R.id.reputation__progress_details_1) {
            showOverlay(activity, R.id.activity_home__Reputation_Details);
            return null;
        }

        // --- Reputation overlay: close (tapping the overlay itself dismisses it) ---
        if (id == R.id.activity_home__Reputation_Details || id == R.id.activity_home__cancel_Reputation_Details) {
            hideOverlay(activity, R.id.activity_home__Reputation_Details);
            return null;
        }

        // --- Global back button ---
        if (id == R.id.Back_Button) {
            activity.finish();
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return null;
        }


        return null;
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    /** Fades in an overlay view, making it visible. */
    private void showOverlay(Activity activity, int overlayId) {
        View overlay = activity.findViewById(overlayId);
        if (overlay != null) NavigationHelper.fadeIn(overlay);
    }

    /** Fades out an overlay view, hiding it. */
    private void hideOverlay(Activity activity, int overlayId) {
        View overlay = activity.findViewById(overlayId);
        if (overlay != null) NavigationHelper.fadeOut(overlay);
    }
}