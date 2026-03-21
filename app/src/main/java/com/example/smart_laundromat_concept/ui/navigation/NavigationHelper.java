package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.example.smart_laundromat_concept.R;

import java.util.Arrays;
import java.util.List;

/**
 * Centralizes navigation execution across the app.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Dispatching navigation requests to appropriate handlers.</li>
 *   <li>Executing activity transitions with animations.</li>
 *   <li>Handling internal view transitions within the same activity.</li>
 * </ul>
 * <p>
 * This class separates navigation execution (how) from navigation decision
 * logic (what), which is handled by {@link NavigatorModule} implementations.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference
 * (e.g., {@link BookingNavigator#handle}) to jump directly to its implementation.
 */
public class NavigationHelper {

    // -------------------------------------------------------------------------
    // Registered Navigators
    // -------------------------------------------------------------------------

    // Navigators are checked in order — first match wins
    private static final List<NavigatorModule> NAVIGATORS = Arrays.asList(
            new AuthNavigator(),
            new MenuNavigator(),
            new BookingNavigator(),
            new SystemNavigator()
    );

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Entry point for handling navigation triggered by a UI interaction.
     * Iterates through registered navigators to find a matching handler.
     *
     * @param activity the current Activity context
     * @param view     the clicked view triggering navigation
     */
    public static void launchPage(Activity activity, View view) {
        int id = view.getId();

        for (NavigatorModule navigator : NAVIGATORS) {
            NavigationRequest request = navigator.handle(activity, id);
            if (request != null) {
                executeNavigation(activity, request);
                return;
            }
        }
    }

    /**
     * Executes an internal view transition within the same Activity.
     * Supports directional slide animations or simple visibility toggling.
     *
     * @param activity the current Activity context
     * @param request  the navigation request containing view references and animation type
     */
    public static void executeInternalTransition(Activity activity, NavigationRequest request) {
        View toShow = request.viewToShow;
        View toHide = request.viewToHide;

        if (request.animation == NavigationRequest.AnimationType.INTERNAL_SLIDE_LEFT) {
            // Show from left, hide to the right
            if (toHide != null) {
                toHide.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_out_right));
                toHide.setVisibility(View.GONE);
            }
            toShow.setVisibility(View.VISIBLE);
            toShow.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_left));

        } else if (request.animation == NavigationRequest.AnimationType.INTERNAL_SLIDE_RIGHT) {
            // Show from right, hide to the left
            if (toHide != null) {
                toHide.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_out_left));
                toHide.setVisibility(View.GONE);
            }
            toShow.setVisibility(View.VISIBLE);
            toShow.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_right));

        } else {
            // Fallback — toggle visibility without animation
            if (toHide != null) toHide.setVisibility(View.GONE);
            if (toShow != null) toShow.setVisibility(View.VISIBLE);
        }
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Executes a NavigationRequest by either starting a new Activity
     * or performing an internal view transition.
     *
     * @param activity the current Activity context
     * @param request  the navigation request describing the action
     */
    private static void executeNavigation(Activity activity, NavigationRequest request) {
        // Case 1 — internal view transition (same activity)
        if (request.viewToShow != null) {
            executeInternalTransition(activity, request);
            return;
        }

        // Case 2 — activity navigation (new screen)
        Intent intent = request.intent != null
                ? request.intent
                : new Intent(activity, request.targetClass);

        String targetName = intent.getComponent() != null
                ? intent.getComponent().getClassName()
                : request.targetClass.getName();

        // Prevent launching the same activity again
        if (activity.getClass().getName().equals(targetName)) {
            return;
        }

        activity.startActivity(intent);

        // Apply transition animation
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