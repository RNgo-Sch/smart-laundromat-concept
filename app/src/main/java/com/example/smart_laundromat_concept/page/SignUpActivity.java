package com.example.smart_laundromat_concept.page;

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

import com.example.smart_laundromat_concept.classes.*;

import com.example.smart_laundromat_concept.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SignUpActivity handles the Account Creation screen.
 * It reuses the activity_main layout but in "Sign Up Mode".
 */
public class SignUpActivity extends AppCompatActivity {

    // UI references for input fields and buttons
    private EditText activity_sign_up__Signup_username_text;
    private EditText activity_sign_up__Signup_password_text;
    private Button activity_sign_up__Create_Account_Button;
    private Button activity_sign_up__Return_to_Signin_Button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Setup screen to use full display (edge-to-edge)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Adjust for system bar height (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Use the utility class to switch the shared layout labels to "Sign Up" mode
        LogInSignUpMode.setup(this, LogInSignUpMode.MODE_SIGNUP);

        // Map layout IDs (from activity_main.xml) to Java variables
        activity_sign_up__Signup_username_text = findViewById(R.id.activity_main__username_text);
        activity_sign_up__Signup_password_text = findViewById(R.id.activity_main__password_text);
        activity_sign_up__Create_Account_Button = findViewById(R.id.activity_main__login_Button);
        activity_sign_up__Return_to_Signin_Button = findViewById(R.id.activity_main__go_to_signup_Button);

        // Logic for creating an account
        activity_sign_up__Create_Account_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = activity_sign_up__Signup_username_text.getText().toString();
                String pass = activity_sign_up__Signup_password_text.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                User newUser = new User(user, pass);

                // 2. Call Supabase API
                SupabaseClient.getApi().createUser(newUser).enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        // Log the HTTP Code (201 = Created, 400 = Bad Request, 403 = RLS Issue)
                        android.util.Log.d("SUPABASE_RES", "Status Code: " + response.code());

                        if (response.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                // This tells you EXACTLY why Postgres rejected the insert
                                String errorBody = response.errorBody().string();
                                android.util.Log.e("SUPABASE_RES", "Error Body: " + errorBody);
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        android.util.Log.e("SUPABASE_RES", "Network Failure: " + t.getMessage());
                    }
                });

                /*
                // Call the mock signup function
                if (LogInSignUpMode.signup(user, pass, "0")) {
                    Toast.makeText(SignUpActivity.this, "Toast: Account Created Successfully!", Toast.LENGTH_LONG).show();
                    launchPage(view); // Return to Login or Home
                } else {
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                }
                 */
            }
        });

        // Logic for switching back to the Login screen
        activity_sign_up__Return_to_Signin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPage(view);
            }
        });
    }

    /**
     * Helper method to call the centralized Navigation logic.
     */
    public void launchPage(View view) {
        Navigation.launchPage(this, view);
    }
}
