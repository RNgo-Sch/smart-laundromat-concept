package com.example.smart_laundromat_concept.page;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.classes.*;

public class MainActivity extends AppCompatActivity {

    private EditText activity_main__username_text;
    private EditText activity_main__password_text;
    private Button activity_main__login_Button;
    private Button activity_main__go_to_signup_Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main__username_text = findViewById(R.id.activity_main__username_text);
        activity_main__password_text = findViewById(R.id.activity_main__password_text);
        activity_main__login_Button = findViewById(R.id.activity_main__login_Button);
        activity_main__go_to_signup_Button = findViewById(R.id.activity_main__go_to_signup_Button);

        activity_main__login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity_main__username_text.getText().toString().equals("user") && activity_main__password_text.getText().toString().equals("1234")) {
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    launchPage(view);

                } else {
                    Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activity_main__go_to_signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPage(view);
            }
        });
    }


//    public void launchPage(View view) {
//
//        Intent intent = null;
//        int id = view.getId();
//
//        //check which button was clicked
//
//        //====================================
//        // Buttons for activity_main.xml
//        //====================================
//        if (id == R.id.activity_main__login_Button) {
//            intent = new Intent(this, HomeActivity.class);
//
//        } else if (id == R.id.activity_main__go_to_signup_Button) {
//            intent = new Intent(this, SignUpActivity.class);
//
//
//        //====================================
//        // Buttons for activity_sign_up.xml
//        //====================================
////        } else if (id == R.id.create_Account_Button_activity_sign_up) {
////            intent = new Intent(this, MainActivity.class);
////            Toast.makeText(MainActivity.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
////
////        } else if (id == R.id.return_to_Signin_Button_acitivity_sign_up) {
////            intent = new Intent(this, MainActivity.class);
//        }
//
//        if (intent != null) {
//            startActivity(intent);
//        }
//    }
    public void launchPage(View view){
        Navigation.launchPage(this, view);
    }
}


