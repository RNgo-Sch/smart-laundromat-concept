package com.example.smart_laundromat_concept.ui.common;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.smart_laundromat_concept.R;

/**
 * UI utility class for managing the shared bottom menu bar across the application.
 * This class centralizes the logic for highlighting the active menu section.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference 
 * (e.g., {@link MenuBarHelper#menuBar}) to jump directly to its implementation.
 */
public class MenuBarHelper {


    /** Identifier representing the Home section of the menu. */
    public static final int HOME = 0;

    /** Identifier representing the Booking section of the menu. */
    public static final int BOOKING = 1;

    /** Identifier representing the Profile section of the menu. */
    public static final int PROFILE = 2;


    /**
     * Updates the visual state of the shared bottom menu bar to reflect the current page.
     * 
     * @param activity The current Activity context.
     * @param menu     The menu item that should be highlighted (HOME, BOOKING, or PROFILE).
     */
    public static void menuBar(Activity activity, int menu){
        // Access the root view content to find the menu bar elements (Note: "MenuBarHelper#menuBar")
        View root = activity.findViewById(android.R.id.content);


        // --- Phase 1: Reference Home Menu UI elements ---
        ImageView homeSelect = root.findViewById(R.id.menu_bar__HomeIconText_Select); 
        ImageView homeIcon = root.findViewById(R.id.menu_bar__Home_Icon);
        TextView homeText = root.findViewById(R.id.menu_bar__Home_Text);


        // --- Phase 2: Reference Booking Menu UI elements ---
        ImageView bookingSelect = root.findViewById(R.id.menu_bar__BookingIconText_Select);
        ImageView bookingIcon = root.findViewById(R.id.menu_bar__Booking_Icon);
        TextView bookingText = root.findViewById(R.id.menu_bar_Booking_Text);


        // --- Phase 3: Reference Profile Menu UI elements ---
        ImageView profileSelect = root.findViewById(R.id.menu_bar___ProfileIconText_Select);
        ImageView profileIcon = root.findViewById(R.id.menu_bar__Profile_Icon);
        TextView profileText = root.findViewById(R.id.menu_bar__Profile_Text);


        // --- Phase 4: Prepare Theme Colors ---
        int black = ContextCompat.getColor(activity, R.color.black);
        int blue = ContextCompat.getColor(activity, R.color.blue);


        // --- Phase 5: RESET UI State (Set all items to default inactive state) ---
        homeSelect.setVisibility(View.INVISIBLE);
        bookingSelect.setVisibility(View.INVISIBLE);
        profileSelect.setVisibility(View.INVISIBLE);

        homeIcon.setColorFilter(black);
        bookingIcon.setColorFilter(black);
        profileIcon.setColorFilter(black);

        homeText.setTextColor(black);
        bookingText.setTextColor(black);
        profileText.setTextColor(black);


        // --- Phase 6: ACTIVATE UI State (Highlight the specific active section) ---
        if (menu == HOME) {
            homeSelect.setVisibility(View.VISIBLE);
            homeIcon.setColorFilter(blue);
            homeText.setTextColor(blue);

        } else if (menu == BOOKING) {
            bookingSelect.setVisibility(View.VISIBLE);
            bookingIcon.setColorFilter(blue);
            bookingText.setTextColor(blue);

        } else if (menu == PROFILE) {
            profileSelect.setVisibility(View.VISIBLE);
            profileIcon.setColorFilter(blue);
            profileText.setTextColor(blue);
        }
    }
}
