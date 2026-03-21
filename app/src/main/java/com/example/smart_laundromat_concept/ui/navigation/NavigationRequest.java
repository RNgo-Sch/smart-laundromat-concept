package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Encapsulates a navigation destination and its transition style.
 * <p>
 * Supports two navigation types:
 * <ul>
 *   <li>Activity navigation — transitioning between different Activity classes.</li>
 *   <li>Internal UI transitions — animating between views within the same screen.</li>
 * </ul>
 */
public class NavigationRequest {

    // -------------------------------------------------------------------------
    // Animation Types
    // -------------------------------------------------------------------------

    /**
     * Defines the visual style of the navigation transition.
     */
    public enum AnimationType {
        /** Slide the new screen in from the right. */
        SLIDE_RIGHT,
        /** Slide the new screen in from the left. */
        SLIDE_LEFT,
        /** Fade between screens. */
        FADE,
        /** Slide a view in from the left within the same screen. */
        INTERNAL_SLIDE_LEFT,
        /** Slide a view in from the right within the same screen. */
        INTERNAL_SLIDE_RIGHT
    }

    // -------------------------------------------------------------------------
    // Activity Navigation Fields
    // -------------------------------------------------------------------------

    /** The destination Activity class. */
    public Class<? extends Activity> targetClass;

    /** An explicit Intent for activity navigation, used when flags are needed. */
    public Intent intent;

    // -------------------------------------------------------------------------
    // Internal Transition Fields
    // -------------------------------------------------------------------------

    /** The view to reveal during an internal transition. */
    public View viewToShow;

    /** The view to hide during an internal transition. */
    public View viewToHide;

    // -------------------------------------------------------------------------
    // Shared Fields
    // -------------------------------------------------------------------------

    /** The animation style to apply during the transition. */
    public AnimationType animation;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Creates a request for standard Activity navigation.
     *
     * @param target the destination Activity class
     * @param anim   the animation style to apply
     */
    public NavigationRequest(Class<? extends Activity> target, AnimationType anim) {
        this.targetClass = target;
        this.animation = anim;
    }

    /**
     * Creates a request for Activity navigation using an explicit Intent.
     * Use this when navigation flags (e.g. FLAG_ACTIVITY_CLEAR_TASK) are needed.
     *
     * @param intent the configured Intent
     * @param anim   the animation style to apply
     */
    public NavigationRequest(Intent intent, AnimationType anim) {
        this.intent = intent;
        this.animation = anim;
    }

    /**
     * Creates a request for an internal view transition within the same screen.
     *
     * @param show the view to reveal
     * @param hide the view to conceal
     * @param anim the animation style to apply
     */
    public NavigationRequest(View show, View hide, AnimationType anim) {
        this.viewToShow = show;
        this.viewToHide = hide;
        this.animation = anim;
    }
}