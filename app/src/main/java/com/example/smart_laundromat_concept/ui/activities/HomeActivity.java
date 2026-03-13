package com.example.smart_laundromat_concept.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.components.Navigation;
import com.example.smart_laundromat_concept.ui.utils.MenuBarHelper;

/**
 * HomeActivity is the main landing page after a user logs in.
 * It displays the primary dashboard of the application.
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Setup full-screen display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Highlight the 'Home' icon in the bottom menu bar
        MenuBarHelper.menuBar(this, MenuBarHelper.HOME);

        // Standard edge-to-edge padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Handles clicks on global UI elements like the menu bar.
     * Delegates to the Navigation class to determine the correct page to open.
     */
    public void launchPage(View view){
        Navigation.launchPage(this, view);
    }
}
