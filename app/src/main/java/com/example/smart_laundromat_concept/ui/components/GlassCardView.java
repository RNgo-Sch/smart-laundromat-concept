package com.example.smart_laundromat_concept.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smart_laundromat_concept.R;

/**
 * A composite view that wraps a {@link LiquidGlassView} with a predefined background.
 * Used for consistent glass-styled cards across the application.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link LiquidGlassView}) to jump directly to its implementation.
 */
public class GlassCardView extends FrameLayout {


    /**
     * Standard constructor for creating the view via code.
     */
    public GlassCardView(@NonNull Context context) {
        super(context);
        init();
    }


    /**
     * Standard constructor for inflating the view from XML.
     */
    public GlassCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    /**
     * Standard constructor for inflating with a default style.
     */
    public GlassCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     * Initializes the view by inflating the predefined glass card layout.
     */
    private void init() {
        // Inflate the layout that uses LiquidGlassView internally
        // (Note: This refers to R.layout.view_glass_card)
        inflate(getContext(), R.layout.layout_view_glass_card, this);
    }
}
