package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * NavigationRequest encapsulates all data needed to perform a navigation action.
 * <p>
 * It supports two primary navigation types:
 * <p>
 * - Activity Navigation: Transitioning between different Activity classes.
 * <p>
 * - Internal UI Transitions: Animating between views within the same screen.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage}
 * to see how these requests are processed.
 */
public class NavigationRequest {

    /**
     * Defines the visual style of the transition.
     */
    public enum AnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, FADE,
        INTERNAL_SLIDE_LEFT, INTERNAL_SLIDE_RIGHT
    }

    // --- ACTIVITY NAVIGATION FIELDS ---
    public Class<? extends Activity> targetClass;
    public Intent intent;

    // --- INTERNAL UI TRANSITION FIELDS ---
    public View viewToShow;
    public View viewToHide;

    // --- SHARED FIELDS ---
    public AnimationType animation;

    /**
     * Constructor for standard Activity navigation.
     *
     * @param target The destination Activity class.
     * @param anim The animation style to apply.
     */
    public NavigationRequest(Class<? extends Activity> target, AnimationType anim) {
        this.targetClass = target;
        this.animation = anim;
    }

    /**
     * Constructor for Activity navigation using an explicit Intent.
     *
     * @param intent The configured Intent.
     * @param anim The animation style to apply.
     */
    public NavigationRequest(Intent intent, AnimationType anim) {
        this.intent = intent;
        this.animation = anim;
    }

    /**
     * Constructor for internal view transitions within the same screen.
     *
     * @param show The view to reveal.
     * @param hide The view to conceal.
     * @param anim The animation style to apply.
     */
    public NavigationRequest(View show, View hide, AnimationType anim) {
        this.viewToShow = show;
        this.viewToHide = hide;
        this.animation = anim;
    }
}
