package com.example.smart_laundromat_concept.ui.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.utils.NavigationHelper;
import com.example.smart_laundromat_concept.ui.utils.MenuBarHelper;

/**
 * ProfileActivity handles the user profile screen.
 * It displays account details, balance, and user settings.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link MenuBarHelper#menuBar}) to jump directly to its implementation.
 */
public class ProfileActivity extends AppCompatActivity {

    /**
     * Initializes the Activity, sets up the layout, and configures the menu bar.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Setup full-screen display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);


        // Highlight the 'Profile' icon in the bottom menu bar.
        // (Hold Cmd/Ctrl + Click on "MenuBarHelper#menuBar" to jump to the method)
        MenuBarHelper.menuBar(this, MenuBarHelper.PROFILE);


        // Standard edge-to-edge padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_profile__root), (v, insets) -> {
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
