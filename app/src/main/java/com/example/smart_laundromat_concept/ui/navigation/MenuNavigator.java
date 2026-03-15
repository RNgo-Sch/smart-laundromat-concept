package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.main.BookingActivity;
import com.example.smart_laundromat_concept.ui.activities.main.HomeActivity;
import com.example.smart_laundromat_concept.ui.activities.profile.ProfileActivity;

/**
 * Handles navigation logic for the bottom menu bar.
 */
public class MenuNavigator {

    public static NavigationRequest handleMenu(Activity activity, int id) {
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
