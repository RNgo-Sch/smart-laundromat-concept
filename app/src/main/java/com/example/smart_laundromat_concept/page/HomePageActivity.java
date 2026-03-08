package com.example.smart_laundromat_concept.page;

import android.os.Bundle;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smart_laundromat_concept.R;
import com.qmdeve.liquidglass.widget.LiquidGlassView;
import com.example.smart_laundromat_concept.classes.*;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
//        LiquidGlassView liquidGlassView = findViewById(R.id.liquidGlassView);
//        ViewGroup content = findViewById(R.id.content_container);
//
//        liquidGlassView.setBlurRadius(30f);
//        liquidGlassView.setCornerRadius(80f);
//
//        liquidGlassView.setTintColorRed(1f);
//        liquidGlassView.setTintColorGreen(1f);
//        liquidGlassView.setTintColorBlue(1f);
//        liquidGlassView.setTintAlpha(0.2f);
//
//        liquidGlassView.post(() -> liquidGlassView.bind(content));
//
//        if (liquidGlassView != null && content != null) {
//            // Configure the glass effect programmatically since XML attributes are not supported
//            liquidGlassView.setBlurRadius(25f);
//            liquidGlassView.setCornerRadius(60f); // approx 24dp in px depending on density
//
//            // Set tint to white with some transparency
//            liquidGlassView.setTintColorRed(1.0f);
//            liquidGlassView.setTintColorGreen(1.0f);
//            liquidGlassView.setTintColorBlue(1.0f);
//            liquidGlassView.setTintAlpha(0.25f); // 25% transparency
//
//            liquidGlassView.post(new Runnable() {
//                @Override
//                public void run() {
//                    liquidGlassView.bind(content);
//                }
//            });

    }
}
