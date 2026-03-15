package com.example.smart_laundromat_concept.ui.activities.auth;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.User;
import com.example.smart_laundromat_concept.data.remote.AuthRepository;
import com.example.smart_laundromat_concept.data.remote.SupabaseClient;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;
import com.example.smart_laundromat_concept.data.session.UserSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * MainActivity handles the Login screen.
 * It uses the shared activity_main layout in "Login Mode".
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link AuthRepository#login}) to jump directly to its implementation.
 */
public class LogInActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_1_main);


        // Adjust padding to avoid UI elements being hidden behind system bars (status/navigation bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        
        // Use the utility class to set the title and button text to "Login" mode.
        // (Hold Cmd/Ctrl + Click on "LoginToggleHelper#setup" to jump to the method)
        AuthUIHelper.setup(this, AuthUIHelper.MODE_LOGIN);

        
        // Initialize UI component references
        etUsername = findViewById(R.id.activity_main__username_text);
        etPassword = findViewById(R.id.activity_main__password_text);
        loginButton = findViewById(R.id.activity_main__login_Button);
        goSignupButton = findViewById(R.id.activity_main__go_to_signup_Button);

        // Set up the Login button logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LogInActivity.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                String query = "eq." + username;

                SupabaseClient.getApi().getUserByUsername(query).enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            User foundUser = response.body().get(0);

                            if (foundUser.password.equals(password)) {
                                // SUCCESS: Save the session!
                                saveSession(foundUser.username);

                                Toast.makeText(LogInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                // --- SESSION MANAGEMENT ---
                                // Save username to the global session so any screen can access it
                                UserSession.getInstance().setUsername(username);
                                launchPage(view);
                            } else {
                                Toast.makeText(LogInActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(LogInActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });


            /*
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
             */
            }

            private void saveSession(String username) {
                SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("current_username", username);
                editor.putBoolean("is_logged_in", true);
                editor.apply(); // Saves in the background
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
