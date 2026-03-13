package com.example.smart_laundromat_concept.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smart_laundromat_concept.R;

/**
 * A custom FrameLayout that provides a "Glassmorphism" effect.
 * It features a blurred background (on Android 12+), a semi-transparent fill, 
 * rounded corners, and a gradient stroke/border.
 */
public class LiquidGlassView extends FrameLayout {

    // --- State: Visual Properties ---
    // radius of the blur effect (requires Android 12+)
    private float blurRadius;
    // base color of the glass panel
    private int glassColor;
    // corner radius for rounded rectangles
    private float cornerRadius;
    
    // --- State: Stroke/Border Properties ---
    private float strokeWidth;
    private int strokeColorStart;
    private int strokeColorCenter;
    private int strokeColorEnd;

    // --- State: Drawing Objects (Pre-allocated for performance) ---
    // Paint used for the background fill
    private Paint fillPaint;
    // Paint used for the border stroke
    private Paint strokePaint;
    // Reusable rectangle for drawing bounds
    private RectF rectF;
    
    // --- State: Gradient configuration ---
    // Array to store colors for the border gradient
    private final int[] gradientColors = new int[3];
    // Relative positions for each color in the gradient
    private final float[] gradientPositions = new float[]{0f, 0.5f, 0.98f};



    /**
     * Standard constructor for creating the view via code.
     */
    public LiquidGlassView(@NonNull Context context) {
        super(context);
        init(null);
    }

    /**
     * Standard constructor for inflating the view from XML.
     */
    public LiquidGlassView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * Standard constructor for inflating with a default style.
     */
    public LiquidGlassView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Initialize the view, paints, and custom attributes.
     */
    private void init(@Nullable AttributeSet attrs) {
        // Tell the system this ViewGroup will draw its own content (onDraw will be called)
        setWillNotDraw(false);
        
        // Initialize drawing tools
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        
        rectF = new RectF();

        // Load custom attributes from XML if they exist
        if (attrs != null) {
            try (TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LiquidGlassView)) {
                // Read blur, color, and radius settings
                blurRadius = a.getDimension(R.styleable.LiquidGlassView_lg_blur_radius, 25f);
                glassColor = a.getColor(R.styleable.LiquidGlassView_lg_color, Color.parseColor("#4DFFFFFF"));
                cornerRadius = a.getDimension(R.styleable.LiquidGlassView_lg_corner_radius, 30f);
                
                // Read border/stroke settings
                strokeWidth = a.getDimension(R.styleable.LiquidGlassView_lg_stroke_width, 0f);
                strokeColorStart = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_start, Color.parseColor("#1AFFFFFF"));
                strokeColorCenter = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_center, Color.WHITE);
                strokeColorEnd = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_end, Color.parseColor("#1AFFFFFF"));
            }
        }

        // Prepare the gradient color array
        gradientColors[0] = strokeColorStart;
        gradientColors[1] = strokeColorCenter;
        gradientColors[2] = strokeColorEnd;

        // Configure the stroke width
        strokePaint.setStrokeWidth(strokeWidth);

        // Define the view's outline (used for clipping and shadows)
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // Match the outline to our corner radius
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
        // Ensure child views don't draw outside the rounded corners
        setClipToOutline(true);

        // Apply the hardware-accelerated blur effect (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && blurRadius > 0) {
            setRenderEffect(RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP));
        }

    }

    /**
     * Called whenever the view's size changes.
     * This is the best place to create or update Shaders (Gradients).
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        

        
        // Re-calculate the border gradient based on the new dimensions
        if (strokeWidth > 0 && w > 0 && h > 0) {
            LinearGradient gradient = new LinearGradient(
                    w, 0,                   // Start point (top right)
                    w * 0.5f, h,            // End point (bottom middle)
                    gradientColors,         // Colors
                    gradientPositions,      // Positions
                    Shader.TileMode.CLAMP   // How to handle space outside bounds
            );
            strokePaint.setShader(gradient);
        }
    }

    /**
     * Handles the actual drawing of the glass panel and its border.
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        
        // Step 1: Adjust the drawing rectangle to account for the stroke width
        float halfStroke = strokeWidth / 2f;
        rectF.set(halfStroke, halfStroke, getWidth() - halfStroke, getHeight() - halfStroke);

        // Step 2: Draw the semi-transparent "glass" background fill
        fillPaint.setColor(glassColor);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, fillPaint);
        
        // Step 3: Draw the shiny gradient border/stroke if width > 0
        if (strokeWidth > 0) {
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, strokePaint);
        }
    }
}
