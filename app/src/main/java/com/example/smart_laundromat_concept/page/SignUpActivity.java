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

                // Call the mock signup function
                if (LogInSignUpMode.signup(user, pass, "0")) {
                    Toast.makeText(SignUpActivity.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
                    launchPage(view); // Return to Login or Home
                } else {
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                }
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
