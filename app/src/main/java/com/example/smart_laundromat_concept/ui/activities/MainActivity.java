package com.example.smart_laundromat_concept.ui.activities;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.remote.AuthRepository;
import com.example.smart_laundromat_concept.ui.utils.LoginToggleHelper;
import com.example.smart_laundromat_concept.ui.utils.NavigationHelper;
import com.example.smart_laundromat_concept.ui.utils.UserSession;


/**
 * MainActivity handles the Login screen.
 * It uses the shared activity_main layout in "Login Mode".
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link AuthRepository#login}) to jump directly to its implementation.
 */
public class MainActivity extends AppCompatActivity {


    // UI elements for the login form
    private EditText etUsername;
    private EditText etPassword;
    private Button loginButton;
    private Button goSignupButton;


    /**
     * Initializes the Activity, sets up the layout, and configures the UI mode.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Enable edge-to-edge display (system bars overlap with app content)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Adjust padding to avoid UI elements being hidden behind system bars (status/navigation bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        
        // Use the utility class to set the title and button text to "Login" mode.
        // (Hold Cmd/Ctrl + Click on "LoginToggleHelper#setup" to jump to the method)
        LoginToggleHelper.setup(this, LoginToggleHelper.MODE_LOGIN);

        
        // Initialize UI component references
        etUsername = findViewById(R.id.activity_main__username_text);
        etPassword = findViewById(R.id.activity_main__password_text);
        loginButton = findViewById(R.id.activity_main__login_Button);
        goSignupButton = findViewById(R.id.activity_main__go_to_signup_Button);

        
        // Set up the Login button logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                
                // Validate credentials via the AuthRepository.
                // (Hold Cmd/Ctrl + Click on "AuthRepository#login" to jump to the logic)
                if (AuthRepository.login(username, password)) {
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    // --- SESSION MANAGEMENT ---
                    // Save username to the global session so any screen can access it
                    UserSession.getInstance().setUsername(username);

                    // Navigate to Home screen
                    launchPage(view);

                } else {
                    Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        
        // Set up the "Go to Sign Up" button logic
        goSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPage(view); // Navigation class handles switching to SignUpActivity
            }
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
