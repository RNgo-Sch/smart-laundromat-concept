package com.example.smart_laundromat_concept.classes;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.smart_laundromat_concept.R;

public class UI {

    public static final int HOME = 0;
    public static final int BOOKING = 1;
    public static final int PROFILE = 2;

    public static void menuBar(Activity activity, int menu){

        View root = activity.findViewById(android.R.id.content);

        ImageView homeSelect = root.findViewById(R.id.menu_bar__HomeIconText_Select);
        ImageView homeIcon = root.findViewById(R.id.menu_bar__Home_Icon);
        TextView homeText = root.findViewById(R.id.menu_bar__Home_Text);

        ImageView bookingSelect = root.findViewById(R.id.menu_bar__BookingIconText_Select);
        ImageView bookingIcon = root.findViewById(R.id.menu_bar__Booking_Icon);
        TextView bookingText = root.findViewById(R.id.menu_bar_Booking_Text);

        ImageView profileSelect = root.findViewById(R.id.menu_bar___ProfileIconText_Select);
        ImageView profileIcon = root.findViewById(R.id.menu_bar__Profile_Icon);
        TextView profileText = root.findViewById(R.id.menu_bar__Profile_Text);

        int black = ContextCompat.getColor(activity, R.color.black);
        int blue = ContextCompat.getColor(activity, R.color.blue);

        // Reset
        homeSelect.setVisibility(View.INVISIBLE);
        bookingSelect.setVisibility(View.INVISIBLE);
        profileSelect.setVisibility(View.INVISIBLE);

        homeIcon.setColorFilter(black);
        bookingIcon.setColorFilter(black);
        profileIcon.setColorFilter(black);

        homeText.setTextColor(black);
        bookingText.setTextColor(black);
        profileText.setTextColor(black);

        // Activate selected menu
        if(menu == HOME){

            homeSelect.setVisibility(View.VISIBLE);
            homeIcon.setColorFilter(blue);
            homeText.setTextColor(blue);

        }
        else if(menu == BOOKING){

            bookingSelect.setVisibility(View.VISIBLE);
            bookingIcon.setColorFilter(blue);
            bookingText.setTextColor(blue);

        }
        else if(menu == PROFILE){

            profileSelect.setVisibility(View.VISIBLE);
            profileIcon.setColorFilter(blue);
            profileText.setTextColor(blue);

        }
    }
}