package com.example.smart_laundromat_concept.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smart_laundromat_concept.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }
    public void launchPage(View view) {

        Intent intent = null;
        int id = view.getId();

        //====================================
        // Buttons for activity_home_page.xml
        //====================================
        if (id == R.id.activity_booking___LinearLayout_Booking) {
            intent = new Intent(this, BookingActivity.class);

        } else if (id == R.id.activity_booking___LinearLayout_Profile) {
            intent = new Intent(this, ProfileActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

}
