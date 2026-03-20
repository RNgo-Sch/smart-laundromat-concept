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
 * A custom FrameLayout that provides a "Glassmorphism" UI effect.
 * Features a hardware-accelerated blur (Android 12+), semi-transparent fill, 
 * rounded corners, and a gradient border.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link RenderEffect#createBlurEffect}) to jump directly to its implementation.
 */
public class LiquidGlassView extends FrameLayout {


    // --- State: Visual Properties ---

    // Radius of the blur effect (requires Android 12+)
    private float blurRadius;

    // Base color of the glass panel
    private int glassColor;

    // Corner radius for rounded rectangles
    private float cornerRadius;

    // Determines if the view should be rendered as an oval/circle
    private boolean isOval;


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
        // Step 1: Tell the system this ViewGroup will draw its own content
        setWillNotDraw(false);
        
        
        // Step 2: Initialize drawing tools
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        
        rectF = new RectF();


        // Step 3: Load custom attributes from XML if they exist
        if (attrs != null) {
            try (TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LiquidGlassView)) {
                blurRadius = a.getDimension(R.styleable.LiquidGlassView_lg_blur_radius, 25f);
                glassColor = a.getColor(R.styleable.LiquidGlassView_lg_color, Color.parseColor("#4DFFFFFF"));
                cornerRadius = a.getDimension(R.styleable.LiquidGlassView_lg_corner_radius, 30f);
                
                // If corner radius is very high, treat as oval/circle
                isOval = cornerRadius >= 999;

                strokeWidth = a.getDimension(R.styleable.LiquidGlassView_lg_stroke_width, 0f);
                strokeColorStart = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_start, Color.parseColor("#1AFFFFFF"));
                strokeColorCenter = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_center, Color.WHITE);
                strokeColorEnd = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_end, Color.parseColor("#1AFFFFFF"));
            }
        }


        // Step 4: Configure paint states
        gradientColors[0] = strokeColorStart;
        gradientColors[1] = strokeColorCenter;
        gradientColors[2] = strokeColorEnd;

        strokePaint.setStrokeWidth(strokeWidth);


        // Step 5: Define clipping and outlines
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                if (isOval) {
                    // Use oval outline for circular shapes
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                } else {
                    // Standard rounded rectangle
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
                }
            }
        });
        setClipToOutline(true);


        // Step 6: Apply blur RenderEffect (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && blurRadius > 0) {
            setRenderEffect(RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP));
        }
    }


    /**
     * Called whenever the view's size changes.
     * Re-calculates shaders to fit the new dimensions.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        
        // Re-calculate the border gradient based on the new dimensions
        if (strokeWidth > 0 && w > 0 && h > 0) {
            LinearGradient gradient = new LinearGradient(
                    w, 0,                // Start point (top right)
                    w * 0.5f, h,            // End point (bottom middle)
                    gradientColors,         // Colors
                    gradientPositions,      // Positions
                    Shader.TileMode.CLAMP   // Tile mode
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
        
        
        // Adjust the drawing rectangle to account for the stroke width
        float halfStroke = strokeWidth / 2f;
        rectF.set(halfStroke, halfStroke, getWidth() - halfStroke, getHeight() - halfStroke);


        // Draw the glass background
        fillPaint.setColor(glassColor);
        
        if (isOval) {
            canvas.drawOval(rectF, fillPaint);
            if (strokeWidth > 0) {
                canvas.drawOval(rectF, strokePaint);
            }
        } else {
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, fillPaint);
            if (strokeWidth > 0) {
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, strokePaint);
            }
        }
    }
}
