package com.example.smart_laundromat_concept.classes;

import android.view.ViewGroup;
import com.qmdeve.liquidglass.widget.LiquidGlassView;

public class GlassManager {

    public static void applyGlass(LiquidGlassView glass, ViewGroup content){

        if(glass == null || content == null) return;

        glass.setBlurRadius(30f);
        glass.setCornerRadius(80f);

        glass.setTintColorRed(1f);
        glass.setTintColorGreen(1f);
        glass.setTintColorBlue(1f);
        glass.setTintAlpha(0.2f);

        glass.post(() -> glass.bind(content));
    }
}