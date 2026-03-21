package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;

/**
 * Contract for all navigation handler modules in the application.
 * <p>
 * Each implementation is responsible for handling a specific domain of navigation
 * (e.g., authentication, menu bar, booking, system actions).
 * <p>
 * Registered modules are checked in order by {@link NavigationHelper#launchPage}.
 * The first module to return a non-null {@link NavigationRequest} wins.
 */
public interface NavigatorModule {

    /**
     * Handles a navigation event triggered by a view click.
     *
     * @param activity the current Activity context
     * @param id       the resource ID of the clicked view
     * @return a {@link NavigationRequest} if this module handles the ID, otherwise null
     */
    NavigationRequest handle(Activity activity, int id);
}