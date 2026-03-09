package com.example.smart_laundromat_concept.classes;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.smart_laundromat_concept.R;

/**
 * UI utility class for managing common interface elements across the application.
 */
public class UI {

    // Identifiers for the different menu sections
    public static final int HOME = 0;
    public static final int BOOKING = 1;
    public static final int PROFILE = 2;

    /**
     * Updates the visual state of the shared bottom menu bar to reflect the current page.
     *
     * @param activity The current activity.
     * @param menu     The menu item that should be highlighted (HOME, BOOKING, or PROFILE).
     */
    public static void menuBar(Activity activity, int menu){

        // Access the root view content to find the menu bar elements
        View root = activity.findViewById(android.R.id.content);

        //---------------------------------------------------------
        // 1. Reference Home Menu elements
        //---------------------------------------------------------
        ImageView homeSelect = root.findViewById(R.id.menu_bar__HomeIconText_Select); // The background highlight
        ImageView homeIcon = root.findViewById(R.id.menu_bar__Home_Icon);
        TextView homeText = root.findViewById(R.id.menu_bar__Home_Text);

        //---------------------------------------------------------
        // 2. Reference Booking Menu elements
        //---------------------------------------------------------
        ImageView bookingSelect = root.findViewById(R.id.menu_bar__BookingIconText_Select);
        ImageView bookingIcon = root.findViewById(R.id.menu_bar__Booking_Icon);
        TextView bookingText = root.findViewById(R.id.menu_bar_Booking_Text);

        //---------------------------------------------------------
        // 3. Reference Profile Menu elements
        //---------------------------------------------------------
        ImageView profileSelect = root.findViewById(R.id.menu_bar___ProfileIconText_Select);
        ImageView profileIcon = root.findViewById(R.id.menu_bar__Profile_Icon);
        TextView profileText = root.findViewById(R.id.menu_bar__Profile_Text);

        // Define colors from resources
        int black = ContextCompat.getColor(activity, R.color.black);
        int blue = ContextCompat.getColor(activity, R.color.blue);

        //---------------------------------------------------------
        // 4. RESET: Turn all icons/text to default (Inactive)
        //---------------------------------------------------------
        homeSelect.setVisibility(View.INVISIBLE);
        bookingSelect.setVisibility(View.INVISIBLE);
        profileSelect.setVisibility(View.INVISIBLE);

        homeIcon.setColorFilter(black);
        bookingIcon.setColorFilter(black);
        profileIcon.setColorFilter(black);

        homeText.setTextColor(black);
        bookingText.setTextColor(black);
        profileText.setTextColor(black);

        //---------------------------------------------------------
        // 5. ACTIVATE: Highlight the selected menu item
        //---------------------------------------------------------
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