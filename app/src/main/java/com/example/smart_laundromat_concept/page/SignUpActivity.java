package com.example.smart_laundromat_concept.page;

import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {

    EditText activity_sign_up__Signup_username_text;
    EditText activity_sign_up__Signup_password_text;
    Button activity_sign_up__Create_Account_Button;
    Button activity_sign_up__Return_to_Signin_Button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        activity_sign_up__Signup_username_text = findViewById(R.id.activity_sign_up__Signup_username_text);
        activity_sign_up__Signup_password_text = findViewById(R.id.activity_sign_up__Signup_password_text);
        activity_sign_up__Create_Account_Button = findViewById(R.id.activity_sign_up__Create_Account_Button);
        activity_sign_up__Return_to_Signin_Button = findViewById(R.id.activity_sign_up__Return_to_Signin_Button);

        activity_sign_up__Create_Account_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity_sign_up__Signup_username_text.getText().toString().equals("user")) {
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SignUpActivity.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
                    launchPage(view);
                }
            }
        });

        activity_sign_up__Return_to_Signin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPage(view);
            }
        });
    }
    public void launchPage(View view) {

        Intent intent = null;
        int id = view.getId();

        //check which button was clicked

        //====================================
        // Buttons for activity_sign_up.xml
        //====================================
        if (id == R.id.activity_sign_up__Create_Account_Button) {
            intent = new Intent(this, MainActivity.class);

        } else if (id == R.id.activity_sign_up__Return_to_Signin_Button) {
            intent = new Intent(this, MainActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

}