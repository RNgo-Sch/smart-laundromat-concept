package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.smart_laundromat_concept.R;

/**
 * Centralized dispatcher for app-wide navigation.
 * Delegates specific routing logic to domain-specific navigators (Auth, Menu, System).
 */
public class NavigationHelper {

    /**
     * Primary entry point for view clicks. Dispatches to specialized navigators.
     */
    public static void launchPage(Activity activity, View view) {
        int id = view.getId();
        NavigationRequest request;

        // 1. Try Authentication Domain
        request = AuthNavigator.handleAuth(activity, id);
        
        // 2. Try Menu Navigation Domain
        if (request == null) {
            request = MenuNavigator.handleMenu(activity, id);
        }

        // 3. Try System/Utility Domain
        if (request == null) {
            request = SystemNavigator.handleSystem(activity, id);
        }

        // 4. Execute if a valid request was returned
        if (request != null) {
            executeNavigation(activity, request);
        }
    }

    /**
     * Standardized execution engine for all Intents and Animations.
     */
    private static void executeNavigation(Activity activity, NavigationRequest request) {
        Intent intent = request.intent != null ? request.intent : new Intent(activity, request.targetClass);
        
        // Extract target class name for comparison
        String targetName = intent.getComponent() != null ? 
                intent.getComponent().getClassName() : request.targetClass.getName();

        // Safety: Don't restart the current activity
        if (activity.getClass().getName().equals(targetName)) {
            return;
        }

        activity.startActivity(intent);

        // Apply transition
        if (request.animation != null) {
            switch (request.animation) {
                case FADE:
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                case SLIDE_LEFT:
                    activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                case SLIDE_RIGHT:
                default:
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        }
    }
}
