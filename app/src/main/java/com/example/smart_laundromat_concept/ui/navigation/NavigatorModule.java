package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;

public interface NavigatorModule {
    NavigationRequest handle(Activity activity, int id);
}