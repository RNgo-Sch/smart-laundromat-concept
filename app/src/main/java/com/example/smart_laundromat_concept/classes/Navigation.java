package com.example.smart_laundromat_concept.classes;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.page.*;


public class Navigation {

    public static void launchPage(Activity activity, View view){

        Intent intent = null;
        int id = view.getId();

        //====================================
        // Buttons for activity_main.xml
        //====================================
        if (id == R.id.activity_main__login_Button) {
            intent = new Intent(activity, HomeActivity.class);

        } else if (id == R.id.activity_main__go_to_signup_Button) {
            intent = new Intent(activity, SignUpActivity.class);


        //====================================
        // Buttons for activity_sign_up.xml
        //====================================
        } else if (id == R.id.activity_sign_up__Create_Account_Button ||
                id == R.id.activity_sign_up__Return_to_Signin_Button) {
            intent = new Intent(activity, MainActivity.class);


        //====================================
        // Buttons for menu_bar.xml
        //====================================
        } else if (id == R.id.menu_bar___LinearLayout_Home) {
            intent = new Intent(activity, HomeActivity.class);

        } else if (id == R.id.menu_bar___LinearLayout_Booking) {
            intent = new Intent(activity, BookingActivity.class);

        } else if (id == R.id.menu_bar___LinearLayout_Profile) {
            intent = new Intent(activity, ProfileActivity.class);
        }


        if (intent != null) {
            if (!activity.getClass().getName().equals(intent.getComponent().getClassName())) {
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        }
    }
}
