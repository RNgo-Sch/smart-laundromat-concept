package com.example.smart_laundromat_concept.ui.activities.main;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

/**
 * HomeActivity is the main landing page after a user logs in.
 * It displays the primary dashboard of the application.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link MenuBarHelper#menuBar}) to jump directly to its implementation.
 */
public class HomeActivity extends AppCompatActivity {


    /**
     * Initializes the Activity, sets up the layout, and configures the menu bar.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Setup full-screen display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2_0_home);


        // Highlight the 'Home' icon in the bottom menu bar.
        // (Hold Cmd/Ctrl + Click on "MenuBarHelper#menuBar" to jump to the method)
        MenuBarHelper.menuBar(this, MenuBarHelper.HOME);


        // Apply the underline effect to the 'Change Location' text.
        // (Hold Cmd/Ctrl + Click on "LocationHelper#setupUnderline" to jump to the method)
        LocationHelper.setupUnderline(this);


        // Standard edge-to-edge padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_home__root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     * <p>
     * (Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage} to jump to the method)
     */
    public void launchPage(View view){
        NavigationHelper.launchPage(this, view);
    }
}
