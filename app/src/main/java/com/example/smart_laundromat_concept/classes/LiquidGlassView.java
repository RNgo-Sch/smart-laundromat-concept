package com.example.smart_laundromat_concept.classes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smart_laundromat_concept.R;

public class LiquidGlassView extends FrameLayout {

    private float blurRadius;
    private int glassColor;
    private float cornerRadius;
    
    // Stroke attributes
    private float strokeWidth;
    private int strokeColorStart;
    private int strokeColorCenter;
    private int strokeColorEnd;

    private Paint fillPaint;
    private Paint strokePaint;
    private RectF rectF;

    public LiquidGlassView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public LiquidGlassView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LiquidGlassView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setWillNotDraw(false);
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        
        rectF = new RectF();

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LiquidGlassView);
            blurRadius = a.getDimension(R.styleable.LiquidGlassView_lg_blur_radius, 25f);
            glassColor = a.getColor(R.styleable.LiquidGlassView_lg_color, Color.parseColor("#4DFFFFFF"));
            cornerRadius = a.getDimension(R.styleable.LiquidGlassView_lg_corner_radius, 30f);
            
            strokeWidth = a.getDimension(R.styleable.LiquidGlassView_lg_stroke_width, 0f);
            strokeColorStart = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_start, Color.parseColor("#1AFFFFFF"));
            strokeColorCenter = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_center, Color.WHITE);
            strokeColorEnd = a.getColor(R.styleable.LiquidGlassView_lg_stroke_color_end, Color.parseColor("#1AFFFFFF"));
            
            a.recycle();
        }

        strokePaint.setStrokeWidth(strokeWidth);

        // Strict clipping to keep the blur inside the rounded corners
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
        setClipToOutline(true);

        // Apply the frosted blur effect (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && blurRadius > 0) {
            setRenderEffect(RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        float halfStroke = strokeWidth / 2f;
        rectF.set(halfStroke, halfStroke, getWidth() - halfStroke, getHeight() - halfStroke);

        // Draw background color
        fillPaint.setColor(glassColor);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, fillPaint);
        
        // Draw shining edge (stroke)
        if (strokeWidth > 0) {
            LinearGradient gradient = new LinearGradient(
                    getWidth(), 0, 
                    getWidth() * 0.5f, getHeight(), 
                    new int[]{strokeColorStart, strokeColorCenter, strokeColorEnd},
                    new float[]{0f, 0.5f, 0.98f},
                    Shader.TileMode.CLAMP
            );
            strokePaint.setShader(gradient);
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, strokePaint);
        }
    }
}
