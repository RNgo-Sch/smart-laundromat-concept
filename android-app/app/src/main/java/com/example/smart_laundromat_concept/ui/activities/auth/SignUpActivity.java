package com.example.smart_laundromat_concept.ui.activities.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.User;
import com.example.smart_laundromat_concept.data.remote.AuthRepository;
import com.example.smart_laundromat_concept.data.remote.SupabaseError;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.smart_laundromat_concept.ui.activities.auth.AuthUIHelper.MODE_SIGNUP;

/**
 * SignUpActivity handles the Account Creation screen.
 * It reuses the activity_auth layout in "Sign Up Mode".
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference
 * (e.g., {@link AuthRepository#signup}) to jump directly to its implementation.
 */
public class SignUpActivity extends AppCompatActivity {

    // UI references for input fields and buttons
    private EditText etUsername;
    private EditText etPassword;
    private Button btnCreateAccount;
    private Button btnReturnToLogin;

    /**
     * Initializes the Activity, sets up the layout, and configures the UI for "Sign Up" mode.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup screen to use full display (edge-to-edge)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        // Adjust for system bar height (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configure the shared layout for Sign Up mode
        AuthUIHelper.setup(this, MODE_SIGNUP);

        // Initialize UI component references
        etUsername = findViewById(R.id.activity_main__username_text);
        etPassword = findViewById(R.id.activity_main__password_text);
        btnCreateAccount = findViewById(R.id.activity_main__login_Button);
        btnReturnToLogin = findViewById(R.id.activity_main__Switch_Button);

        // Logic for creating an account
        btnCreateAccount.setOnClickListener(v -> createAccount(v));

        // Logic for switching back to the Login screen
        btnReturnToLogin.setOnClickListener(v -> launchPage(v));
    }

    /**
     * Validates input and delegates account creation to {@link AuthRepository#signup}.
     */
    private void createAccount(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate that fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Delegate the network call to AuthRepository
        AuthRepository.signup(new User(username, password), new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // Guard against the activity being destroyed during the network call
                if (isFinishing() || isDestroyed()) return;

                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Account created!", Toast.LENGTH_SHORT).show();

                    // Save the created user to the session
                    List<User> body = response.body();
                    if (body != null && !body.isEmpty()) {
                        UserSession.getInstance().setCurrentUser(body.get(0));
                    } else {
                        // Fallback if Supabase response body is empty
                        UserSession.getInstance().setCurrentUser(new User(username, password));
                    }

                    launchPage(view);

                } else {
                    handleSignupError(response);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Parses the Supabase error response and shows the appropriate message.
     *
     * @param response the failed Retrofit response
     */
    private void handleSignupError(Response<List<User>> response) {
        try {
            String errorBody = response.errorBody().string();
            SupabaseError error = new Gson().fromJson(errorBody, SupabaseError.class);

            if (error.code.equals("23505")) {
                Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
            } else if (error.code.equals("22P02")) {
                Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     * <p>
     * (Hold Cmd/Ctrl + Click on {@link NavigationHelper#launchPage} to jump to the method)
     */
    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}