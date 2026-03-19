package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.smart_laundromat_concept.R;

import java.util.Arrays;
import java.util.List;

/**
 * NavigationHelper centralizes navigation execution across the app.
 * <p>
 * Responsibilities:
 * <p>
 *- Dispatch navigation requests to appropriate handlers
 * <p>
 * - Execute activity transitions with animations
 * <p>
 * - Handle internal view transitions within the same activity
 * <p>
 * This class separates navigation execution (how) from navigation decision logic (what),
 * which is handled by NavigatorModule implementations.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link BookingNavigator#handle}) to jump directly to its implementation.
 */
public class NavigationHelper {

    // Registered navigator modules for handling specific ID patterns
    private static final List<NavigatorModule> NAVIGATORS = Arrays.asList(
            new AuthNavigator(),
            new MenuNavigator(),
            new BookingNavigator(),
            new SystemNavigator()
    );

    /**
     * Entry point for handling navigation triggered by a UI interaction.
     * <p>
     * Iterates through registered NavigatorModule implementations to determine
     * which one can handle the given view ID.
     *
     * @param activity The current activity context
     * @param view The clicked view triggering navigation
     */
    public static void launchPage(Activity activity, View view) {
        int id = view.getId();

        // Iterate through navigators to find a matching handler
        for (NavigatorModule navigator : NAVIGATORS) {
            NavigationRequest request = navigator.handle(activity, id);
            if (request != null) {
                executeNavigation(activity, request);
                return;
            }
        }
    }

    /**
     * Executes a NavigationRequest.
     * <p>
     * Handles both:
     * <p>
     * - Internal view transitions (within the same activity)
     * <p>
     * - Activity navigation (starting a new activity)
     *
     * @param activity Current activity
     * @param request Navigation request describing the action
     */
    private static void executeNavigation(Activity activity, NavigationRequest request) {
        // Case 1: Handle internal view transitions (same activity)
        if (request.viewToShow != null) {
            executeInternalTransition(activity, request);
            return;
        }

        // Case 2: Handle activity navigation (new screen)
        Intent intent = request.intent != null ? request.intent : new Intent(activity, request.targetClass);
        String targetName = intent.getComponent() != null ? 
                intent.getComponent().getClassName() : request.targetClass.getName();

        // Prevent launching the same activity again
        if (activity.getClass().getName().equals(targetName)) {
            return;
        }

        // Launch target activity
        activity.startActivity(intent);

        // Apply transition animation if specified
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

    /**
     * Executes animated transitions between two views within the same activity.
     * <p>
     * Supports directional slide animations or simple visibility toggling
     * when no animation is specified.
     *
     * @param activity Current activity context
     * @param request Navigation request containing view references and animation type
     */
    public static void executeInternalTransition(Activity activity, NavigationRequest request) {
        View toShow = request.viewToShow;
        View toHide = request.viewToHide;

        if (request.animation == NavigationRequest.AnimationType.INTERNAL_SLIDE_LEFT) {
            // Animate: show view from left, hide current view to the right
            if (toHide != null) {
                toHide.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_out_right));
                toHide.setVisibility(View.GONE);
            }
            toShow.setVisibility(View.VISIBLE);
            toShow.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_left));

        } else if (request.animation == NavigationRequest.AnimationType.INTERNAL_SLIDE_RIGHT) {
            // Animate: show view from right, hide current view to the left
            if (toHide != null) {
                toHide.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_out_left));
                toHide.setVisibility(View.GONE);
            }
            toShow.setVisibility(View.VISIBLE);
            toShow.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_right));
        } else {
            // Fallback: toggle visibility without animation
            if (toHide != null) toHide.setVisibility(View.GONE);
            if (toShow != null) toShow.setVisibility(View.VISIBLE);
        }
    }
}
