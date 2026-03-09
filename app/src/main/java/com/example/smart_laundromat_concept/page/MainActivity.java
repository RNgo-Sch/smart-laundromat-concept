package com.example.smart_laundromat_concept.page;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.classes.*;

/**
 * MainActivity handles the Login screen.
 * It uses the shared activity_main layout in "Login Mode".
 */
public class MainActivity extends AppCompatActivity {

    // UI elements for the login form
    private EditText activity_main__username_text;
    private EditText activity_main__password_text;
    private Button activity_main__login_Button;
    private Button activity_main__go_to_signup_Button;


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

        // Use the utility class to set the title and button text to "Login" mode
        LogInSignUpMode.setup(this, LogInSignUpMode.MODE_LOGIN);

        // Initialize UI component references
        activity_main__username_text = findViewById(R.id.activity_main__username_text);
        activity_main__password_text = findViewById(R.id.activity_main__password_text);
        activity_main__login_Button = findViewById(R.id.activity_main__login_Button);
        activity_main__go_to_signup_Button = findViewById(R.id.activity_main__go_to_signup_Button);

        // Set up the Login button logic
        activity_main__login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = activity_main__username_text.getText().toString();
                String password = activity_main__password_text.getText().toString();

                // Validate credentials via the mode class
                if (LogInSignUpMode.login(username, password)) {
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    launchPage(view); // Navigate to Home
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the "Go to Sign Up" button logic
        activity_main__go_to_signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPage(view); // Navigation class handles switching to SignUpActivity
            }
        });
    }

    /**
     * Delegates page navigation to the centralized Navigation class.
     */
    public void launchPage(View view){
        Navigation.launchPage(this, view);
    }
}
