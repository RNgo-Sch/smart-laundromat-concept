package com.example.smart_laundromat_concept.page;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.classes.*;

/**
 * ProfileActivity displays user information with a "Liquid Glass" effect.
 * The glass box blurs the background image behind it.
 */
public class ProfileActivity extends AppCompatActivity {

    private View glassBox;
    private ImageView blurredBgImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Standard Menu Bar initialization
        UI.menuBar(this, UI.PROFILE);

        glassBox = findViewById(R.id.profile__glass_view);
        blurredBgImage = findViewById(R.id.profile__blurred_bg_image);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //===================================================================================
        // LIQUID GLASS LOGIC:
        // We sync the blurred background image's position to stay fixed relative to the screen.
        // This creates the illusion that the box is a "window" blurring the screen's background.
        //===================================================================================
        if (glassBox != null && blurredBgImage != null) {
            glassBox.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Update the blurred image size to match the screen/root size
                    View root = findViewById(R.id.main);
                    if (root != null) {
                        blurredBgImage.getLayoutParams().width = root.getWidth();
                        blurredBgImage.getLayoutParams().height = root.getHeight();
                        blurredBgImage.requestLayout();
                    }
                    
                    // Sync the position: Counter-translate the image inside the box
                    syncGlassBackground();
                }
            });
        }
    }

    /**
     * Aligns the blurred image inside the glass box with the actual background of the activity.
     */
    private void syncGlassBackground() {
        if (glassBox == null || blurredBgImage == null) return;
        
        // Get the location of the glass box on the screen
        int[] location = new int[2];
        glassBox.getLocationOnScreen(location);
        
        // Move the internal image in the opposite direction of its position on screen
        // to keep it perfectly aligned with the main background.
        blurredBgImage.setTranslationX(-location[0]);
        blurredBgImage.setTranslationY(-location[1]);
    }

    public void launchPage(View view) {
        Navigation.launchPage(this, view);
    }
}
