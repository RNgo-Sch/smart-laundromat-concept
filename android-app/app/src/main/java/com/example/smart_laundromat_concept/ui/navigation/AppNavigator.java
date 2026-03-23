package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;

/**
 * AppNavigator handles global application-level navigation events.
 * <p>
 * This includes handling the initial landing page or redirecting to the Home screen
 * after successful authentication.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage}
 * to see how these requests are processed.
 */
public class AppNavigator implements NavigatorModule {

    /**
     * Processes global navigation requests.
     *
     * @param activity The current Activity context.
     * @param id The ID of the clicked View.
     * @return A {@link NavigationRequest} if the ID is handled, otherwise null.
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {
        // Handle global redirects to Home if needed
        return null;
    }
}
