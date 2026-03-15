package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.Intent;

/**
 * Data class to encapsulate a navigation destination and its transition style.
 * Living in its own file prevents circular dependencies between navigators.
 */
public class NavigationRequest {

    public enum AnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, FADE
    }

    /**
     * The specific Activity class to navigate to.
     * The use of {@code <? extends Activity>} ensures that only classes 
     * inheriting from Android's Activity can be assigned here, providing 
     * compile-time type safety.
     */
    public Class<? extends Activity> targetClass;

    public Intent intent;
    public AnimationType animation;

    public NavigationRequest(Class<? extends Activity> target, AnimationType anim) {
        this.targetClass = target;
        this.animation = anim;
    }

    public NavigationRequest(Intent intent, AnimationType anim) {
        this.intent = intent;
        this.animation = anim;
    }
}
