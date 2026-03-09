package com.example.smart_laundromat_concept.classes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private Paint paint;
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
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LiquidGlassView);
            blurRadius = a.getDimension(R.styleable.LiquidGlassView_lg_blur_radius, 25f);
            glassColor = a.getColor(R.styleable.LiquidGlassView_lg_color, Color.parseColor("#4DFFFFFF"));
            cornerRadius = a.getDimension(R.styleable.LiquidGlassView_lg_corner_radius, 30f);
            a.recycle();
        }

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
        rectF.set(0, 0, getWidth(), getHeight());
        paint.setColor(glassColor);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
    }
}
