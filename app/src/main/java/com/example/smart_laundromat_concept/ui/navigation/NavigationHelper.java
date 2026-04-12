package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.example.smart_laundromat_concept.R;

import java.util.Arrays;
import java.util.List;

/**
 * Central hub for executing all navigation in the app.
 *
 * <p><b>OOP Design — Polymorphism:</b><br>
 * This class holds a list of {@link NavigatorModule} implementations. When a button
 * is tapped, {@link #launchPage} asks each module in turn "can you handle this ID?".
 * The first module that returns a non-null {@link NavigationRequest} wins and the
 * navigation is executed. Adding new navigation destinations only requires adding
 * a new {@link NavigatorModule} — this class never changes.
 *
 * <p><b>Navigation modules (checked in order):</b>
 * <ol>
 *   <li>{@link AuthNavigator}   — Login / Sign Up screens</li>
 *   <li>{@link MenuNavigator}   — Bottom menu bar (Home, Booking, Profile)</li>
 *   <li>{@link BookingNavigator}— Internal tab switching in BookingActivity</li>
 *   <li>{@link SystemNavigator} — Notifications, Location, Logout, Back button</li>
 * </ol>
 */
public class NavigationHelper {

    // -------------------------------------------------------------------------
    // Registered Navigators
    // -------------------------------------------------------------------------

    // Modules are checked in order — the first match handles the request
    private static final List<NavigatorModule> NAVIGATORS = Arrays.asList(
            new AuthNavigator(),
            new MenuNavigator(),
            new BookingNavigator(),
            new SystemNavigator(),
            new HomeNavigator()

    );

    // -------------------------------------------------------------------------
    // Public Methods — Entry Point
    // -------------------------------------------------------------------------

    /**
     * Handles a navigation event triggered by a UI click.
     *
     * <p>Iterates through all registered {@link NavigatorModule} instances and
     * delegates execution to the first one that claims the view ID.
     *
     * @param activity the current Activity context
     * @param view     the view that was clicked
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

    // -------------------------------------------------------------------------
    // Public Methods — View Fade Helpers
    // -------------------------------------------------------------------------

    /**
     * Fades a view in: sets it to VISIBLE and animates its alpha from 0 to 1.
     * Use this to reveal overlays or dialogs.
     *
     * @param view the view to show
     */
    public static void fadeIn(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(200).start();
    }

    /**
     * Fades a view out: animates its alpha from 1 to 0, then sets it to GONE.
     * Use this to dismiss overlays or dialogs.
     *
     * @param view the view to hide
     */
    public static void fadeOut(View view) {
        view.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    // -------------------------------------------------------------------------
    // Package-Private — Used by BookingNavigator for tab animations
    // -------------------------------------------------------------------------

    /**
     * Fades a view's alpha from its current value to 1 without changing visibility.
     * Used by {@link BookingNavigator} to animate the active tab button.
     *
     * @param view the view to animate
     */
    static void fadeInAlpha(View view) {
        view.setAlpha(0f);
        view.animate().alpha(1f).setDuration(200).start();
    }

    // -------------------------------------------------------------------------
    // Public Methods — Internal Transition
    // -------------------------------------------------------------------------

    /**
     * Executes an internal view transition (sliding between views in the same Activity).
     * Used by {@link BookingNavigator} to animate between the washer and dryer containers.
     *
     * @param activity the current Activity context
     * @param request  the request describing which views to show/hide and which animation to use
     */
    public static void executeInternalTransition(Activity activity, NavigationRequest request) {
        View toShow = request.viewToShow;
        View toHide = request.viewToHide;

        if (request.animation == NavigationRequest.AnimationType.INTERNAL_SLIDE_LEFT) {
            // Slide new view in from left, slide old view out to right
            if (toHide != null) {
                toHide.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_out_right));
                toHide.setVisibility(View.GONE);
            }
            toShow.setVisibility(View.VISIBLE);
            toShow.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_left));

        } else if (request.animation == NavigationRequest.AnimationType.INTERNAL_SLIDE_RIGHT) {
            // Slide new view in from right, slide old view out to left
            if (toHide != null) {
                toHide.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_out_left));
                toHide.setVisibility(View.GONE);
            }
            toShow.setVisibility(View.VISIBLE);
            toShow.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_right));

        } else {
            // Fallback — no animation, just toggle visibility
            if (toHide != null) toHide.setVisibility(View.GONE);
            if (toShow != null) toShow.setVisibility(View.VISIBLE);
        }
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Executes a {@link NavigationRequest} — either starting a new Activity
     * or performing an internal view transition within the same Activity.
     *
     * @param activity the current Activity context
     * @param request  the navigation request to execute
     */
    private static void executeNavigation(Activity activity, NavigationRequest request) {

        // Case 1 — internal view transition (same Activity, e.g. tab switch)
        if (request.viewToShow != null) {
            executeInternalTransition(activity, request);
            return;
        }

        // Case 2 — start a new Activity
        Intent intent = (request.intent != null)
                ? request.intent
                : new Intent(activity, request.targetClass);

        // Determine the target class name for duplicate-launch prevention
        String targetName = (intent.getComponent() != null)
                ? intent.getComponent().getClassName()
                : request.targetClass.getName();

        // Do not relaunch the same Activity (avoids duplicate screens)
        if (activity.getClass().getName().equals(targetName)) return;

        activity.startActivity(intent);

        // Apply the transition animation
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