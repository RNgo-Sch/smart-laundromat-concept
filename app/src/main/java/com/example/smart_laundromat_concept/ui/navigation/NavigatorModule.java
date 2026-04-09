package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;

/**
 * Contract that all navigation handler modules must implement.
 *
 * <p><b>OOP Design — Interface & Polymorphism:</b><br>
 * Each implementation handles one domain of navigation (auth, menu, booking, system).
 * {@link NavigationHelper} holds a list of these and calls {@link #handle} on each one.
 * This means adding new navigation never requires changing {@link NavigationHelper} —
 * you simply add a new implementation of this interface.
 *
 * @see AuthNavigator
 * @see MenuNavigator
 * @see BookingNavigator
 * @see SystemNavigator
 */
public interface NavigatorModule {

    /**
     * Attempts to handle a navigation event for the given view ID.
     *
     * @param activity the current Activity context
     * @param id       the resource ID of the clicked view
     * @return a {@link NavigationRequest} if this module handles the ID, otherwise null
     */
    NavigationRequest handle(Activity activity, int id);
}