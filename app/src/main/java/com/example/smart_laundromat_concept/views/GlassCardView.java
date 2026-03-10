package com.example.smart_laundromat_concept.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class GlassCardView extends FrameLayout {

    public GlassCardView(Context context) {
        super(context);
        init();
    }

    public GlassCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlassCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(),
                com.example.smart_laundromat_concept.R.layout.view_glass_card,
                this);
    }
}