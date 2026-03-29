package com.example.smart_laundromat_concept.ui.activities.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.User;
import com.example.smart_laundromat_concept.data.remote.repository.AuthRepository;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.navigation.AuthNavigator;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.smart_laundromat_concept.ui.activities.auth.AuthUIHelper.MODE_LOGIN;

/**
 * LogInActivity handles the Login screen.
 * It uses the shared activity_auth layout in "Login Mode".
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference
 * (e.g., {@link AuthRepository#login}) to jump directly to its implementation.
 */
public class LogInActivity extends AppCompatActivity {

    // UI elements for the login form
    private EditText etUsername;
    private EditText etPassword;
    private Button loginButton;
    private Button switchButton;
    private ProgressBar progressBar;

    /**
     * Initializes the Activity, sets up the layout, and configures the UI mode.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        AuthNavigator.resetNavigation();

        // Adjust padding to avoid UI elements being hidden behind system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configure the shared layout for Login mode
        AuthUIHelper.setup(this, MODE_LOGIN);

        // Initialize UI component references
        etUsername = findViewById(R.id.activity_main__username_text);
        etPassword = findViewById(R.id.activity_main__password_text);
        loginButton = findViewById(R.id.activity_main__login_Button);
        switchButton = findViewById(R.id.activity_main__Switch_Button);
        progressBar = findViewById(R.id.activity_auth__progress);

        // Set up button click listeners
        loginButton.setOnClickListener(v -> handleLogin(v));
        switchButton.setOnClickListener(v -> launchPage(v));
    }

    // -------------------------------------------------------------------------
    // Login Logic
    // -------------------------------------------------------------------------

    /**
     * Validates input and delegates login to {@link AuthRepository#login}.
     *
     * @param view the clicked view, passed to navigation after successful login
     */
    private void handleLogin(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate that fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state and prevent double clicks
        setLoading(true);

        // Delegate the network call to AuthRepository
        AuthRepository.login(username, new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // Guard against the activity being destroyed during the network call
                if (isFinishing() || isDestroyed()) return;

                // Restore normal state
                setLoading(false);

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    User foundUser = response.body().get(0);

                    if (foundUser.getPassword().equals(password)) {
                        //Toast.makeText(LogInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // Save the full user object to the session
                        UserSession.getInstance().setCurrentUser(foundUser);
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
                setLoading(false);
                Toast.makeText(LogInActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // -------------------------------------------------------------------------
    // UI Helpers
    // -------------------------------------------------------------------------

    /**
     * Toggles the loading state of the login form.
     * Disables the button and shows a spinner while the network call is running.
     *
     * @param isLoading true to show loading, false to restore normal state
     */
    private void setLoading(boolean isLoading) {
        if (isLoading) {
            loginButton.setEnabled(false);
            loginButton.setText(R.string.auth_logging_in);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            loginButton.setEnabled(true);
            loginButton.setText(R.string.auth_login);
            progressBar.setVisibility(View.GONE);
        }
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     * <p>
     * (Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage} to jump to the method)
     */
    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}