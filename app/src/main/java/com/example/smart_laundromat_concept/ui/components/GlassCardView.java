package com.example.smart_laundromat_concept.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smart_laundromat_concept.R;

/**
 * A composite view that wraps a LiquidGlassView with a predefined background.
 * Used for consistent glass-styled cards across the app.
 */
public class GlassCardView extends FrameLayout {

    public GlassCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public GlassCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlassCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Inflate the layout that uses LiquidGlassView internally
        inflate(getContext(), R.layout.view_glass_card, this);
    }
}
