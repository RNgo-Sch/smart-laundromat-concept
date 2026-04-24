package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.main.booking.BookingActivity;

public class HomeNavigator implements NavigatorModule {
    @Override
    public NavigationRequest handle(Activity activity, int id) {
        if (id == R.id.active__view_details) {
            return new NavigationRequest(BookingActivity.class, NavigationRequest.AnimationType.SLIDE_RIGHT);
        }
        return null;
    }
}

