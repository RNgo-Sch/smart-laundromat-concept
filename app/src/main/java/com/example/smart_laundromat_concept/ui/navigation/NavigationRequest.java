package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Data class to encapsulate a navigation destination and its transition style.
 */
public class NavigationRequest {

    public enum AnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, FADE,
        INTERNAL_SLIDE_LEFT, INTERNAL_SLIDE_RIGHT // New types for view-level animations
    }

    public Class<? extends Activity> targetClass;
    public Intent intent;
    public AnimationType animation;

    // Fields for internal UI transitions
    public View viewToShow;
    public View viewToHide;

    public NavigationRequest(Class<? extends Activity> target, AnimationType anim) {
        this.targetClass = target;
        this.animation = anim;
    }

    public NavigationRequest(Intent intent, AnimationType anim) {
        this.intent = intent;
        this.animation = anim;
    }

    /**
     * Constructor for internal view transitions.
     */
    public NavigationRequest(View show, View hide, AnimationType anim) {
        this.viewToShow = show;
        this.viewToHide = hide;
        this.animation = anim;
    }
}
