package com.example.smart_laundromat_concept.ui.activities.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;
import com.example.smart_laundromat_concept.data.session.UserSession;

/**
 * ProfileActivity handles the user profile screen.
 * It displays account details, balance, and user settings.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link MenuBarHelper#menuBar}) to jump directly to its implementation.
 */
public class ProfileActivity extends AppCompatActivity {

    /**
     * Initializes the Activity and sets up the static layout elements.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Setup full-screen display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);


        // Highlight the 'Profile' icon in the bottom menu bar
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
     * Called every time the Activity becomes visible.
     * Ensures the username and avatar are always up-to-date with the current session.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }


    /**
     * Retrieves the username from the global session and updates the UI.
     */
    private void loadUserData() {
        // Retrieve the username from the global user session (Note: UserSession#getUsername)
        String username = UserSession.getInstance().getUsername();
        
        // Update the UI if a user is logged in
        if (username != null && !username.isEmpty()) {
            TextView tvUsernameTitle = findViewById(R.id.activity_profile__username_title);
            TextView tvLetterCircle = findViewById(R.id.activity_profile__letter_circle);
            
            // Set the username text
            tvUsernameTitle.setText(username);
            
            // Set the first letter of the username in the avatar circle
            String firstLetter = username.substring(0, 1).toUpperCase();
            tvLetterCircle.setText(firstLetter);
        }
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
