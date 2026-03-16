package com.example.smart_laundromat_concept.ui.activities.auth;

import android.os.Bundle;
import android.util.Log;
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
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SignUpActivity handles the Account Creation screen.
 * It reuses the activity_main layout but in "Sign Up Mode".
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


        // Use the utility class to switch the shared layout labels to "Sign Up" mode.
        // (Hold Cmd/Ctrl + Click on "LoginToggleHelper#setup" to jump to the method)
        AuthUIHelper.setup(this, AuthUIHelper.MODE_SIGNUP);


        // Map layout IDs (from activity_main.xml) to Java variables
        etUsername = findViewById(R.id.activity_main__username_text);
        etPassword = findViewById(R.id.activity_main__password_text);
        btnCreateAccount = findViewById(R.id.activity_main__login_Button);
        btnReturnToLogin = findViewById(R.id.activity_main__go_to_signup_Button);


        // Logic for creating an account
        btnCreateAccount.setOnClickListener(v -> createAccount(v));


        // Logic for switching back to the Login screen
        btnReturnToLogin.setOnClickListener(v -> launchPage(v));
    }

    /**
     * Handles the account creation process by validating input and calling the repository.
     */
    private void createAccount(View view) {
        String user = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use the repository to handle the network logic (Note: AuthRepository#signup)
        AuthRepository.signup(new User(user, password), new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("SUPABASE_RES", "Status Code: " + response.code());
                String errorMsg= "";
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                    // --- SESSION MANAGEMENT ---
                    // Save username to the global session so any screen can access it
                    UserSession.getInstance().setUsername(user);

                    launchPage(view); // Navigation class handles switching to MainActivity
                } else {
                    try {
                        String errorBody = response.errorBody().string();

                        Log.e("SUPABASE_RES", "Signup failed: " + errorBody);

                        Gson gson = new Gson();
                        SupabaseError error = gson.fromJson(errorBody, SupabaseError.class);
                        String errorCode = error.code;

                        if (errorCode.equals("23505")) {
                            Toast.makeText(SignUpActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                        }
                        else if (errorCode.equals("22P02")) {
                            Toast.makeText(SignUpActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("SUPABASE_RES", "Network Failure: " + t.getMessage());
                Toast.makeText(SignUpActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
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
