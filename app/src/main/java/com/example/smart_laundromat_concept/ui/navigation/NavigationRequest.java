package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Describes a navigation action — either starting a new Activity or
 * animating between views within the same Activity.
 *
 * <p>This is a plain data object (no logic). {@link NavigationHelper} reads it
 * and decides how to execute the transition.
 *
 * <p>There are three ways to construct a request:
 * <ol>
 *   <li><b>Activity by class</b> — use {@link #NavigationRequest(Class, AnimationType)}</li>
 *   <li><b>Activity by Intent</b> — use {@link #NavigationRequest(Intent, AnimationType)}
 *       when you need flags like {@code FLAG_ACTIVITY_CLEAR_TASK}</li>
 *   <li><b>Internal view transition</b> — use
 *       {@link #NavigationRequest(View, View, AnimationType)} to slide or swap views
 *       inside the same Activity</li>
 * </ol>
 */
public class NavigationRequest {

    // -------------------------------------------------------------------------
    // Animation Types
    // -------------------------------------------------------------------------

    /** The visual style of the navigation transition. */
    public enum AnimationType {
        /** Slide the new screen in from the right (standard forward navigation). */
        SLIDE_RIGHT,
        /** Slide the new screen in from the left (back navigation). */
        SLIDE_LEFT,
        /** Fade between screens (used for menu bar transitions). */
        FADE,
        /** Slide a view in from the left within the same screen. */
        INTERNAL_SLIDE_LEFT,
        /** Slide a view in from the right within the same screen. */
        INTERNAL_SLIDE_RIGHT
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    // --- Activity navigation ---

    /** Target Activity class (used when navigating without flags). */
    public Class<? extends Activity> targetClass;

    /** Explicit Intent (used when navigation flags are needed). */
    public Intent intent;

    // --- Internal view transition ---

    /** The view to make visible during an internal transition. */
    public View viewToShow;

    /** The view to hide during an internal transition. */
    public View viewToHide;

    // --- Shared ---

    /** The animation to apply during the transition. */
    public AnimationType animation;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Creates a request to navigate to another Activity by class.
     *
     * @param target the destination Activity class
     * @param anim   the transition animation to apply
     */
    public NavigationRequest(Class<? extends Activity> target, AnimationType anim) {
        this.targetClass = target;
        this.animation   = anim;
    }

    /**
     * Creates a request to navigate using an explicit Intent.
     * Use this when you need to set flags (e.g. clear the back stack on logout).
     *
     * @param intent the configured Intent
     * @param anim   the transition animation to apply
     */
    public NavigationRequest(Intent intent, AnimationType anim) {
        this.intent    = intent;
        this.animation = anim;
    }

    /**
     * Creates a request for an internal view transition within the same Activity.
     * Used to animate between the washer and dryer containers in BookingActivity.
     *
     * @param show the view to reveal
     * @param hide the view to conceal
     * @param anim the slide animation to apply
     */
    public NavigationRequest(View show, View hide, AnimationType anim) {
        this.viewToShow = show;
        this.viewToHide = hide;
        this.animation  = anim;
    }
}