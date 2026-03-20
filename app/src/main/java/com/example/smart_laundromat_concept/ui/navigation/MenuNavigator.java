package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.main.HomeActivity;
import com.example.smart_laundromat_concept.ui.activities.main.booking.BookingActivity;
import com.example.smart_laundromat_concept.ui.activities.profile.ProfileActivity;

/**
 * Handles navigation logic for the application's bottom menu bar.
 * <p>
 * This navigator ensures that transitions between primary top-level screens (Home, Booking, Profile)
 * use a consistent fade animation.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any Activity class
 * (e.g., {@link BookingActivity}) to jump to its implementation.
 */
public class MenuNavigator implements NavigatorModule {

    /**
     * Processes menu-related navigation requests based on view IDs.
     *
     * @param activity The current Activity context.
     * @param id The ID of the clicked menu item.
     * @return A {@link NavigationRequest} if the ID is handled, otherwise null.
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {
        if (id == R.id.menu_bar___LinearLayout_Home) {
            return new NavigationRequest(HomeActivity.class, NavigationRequest.AnimationType.FADE);
        }
        if (id == R.id.menu_bar___LinearLayout_Booking) {
            return new NavigationRequest(BookingActivity.class, NavigationRequest.AnimationType.FADE);
        }
        if (id == R.id.menu_bar___LinearLayout_Profile) {
            return new NavigationRequest(ProfileActivity.class, NavigationRequest.AnimationType.FADE);
        }
        return null;
    }
}
