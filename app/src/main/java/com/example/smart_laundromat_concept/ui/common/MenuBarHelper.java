package com.example.smart_laundromat_concept.ui.common;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.smart_laundromat_concept.R;

/**
 * Utility class for managing the shared bottom menu bar across the application.
 * Centralizes the logic for highlighting the active menu section.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference
 * (e.g., {@link MenuBarHelper#menuBar}) to jump directly to its implementation.
 */
public class MenuBarHelper {

    // -------------------------------------------------------------------------
    // Menu Constants
    // -------------------------------------------------------------------------

    /** Identifier representing the Home section of the menu. */
    public static final int HOME = 0;

    /** Identifier representing the Booking section of the menu. */
    public static final int BOOKING = 1;

    /** Identifier representing the Profile section of the menu. */
    public static final int PROFILE = 2;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Updates the visual state of the shared bottom menu bar to reflect the current page.
     *
     * @param activity the current Activity context
     * @param menu     the menu item to highlight (HOME, BOOKING, or PROFILE)
     */
    public static void menuBar(Activity activity, int menu) {
        View root = activity.findViewById(android.R.id.content);

        // --- Reference UI elements ---
        ImageView homeSelect    = root.findViewById(R.id.menu_bar__HomeIconText_Select);
        ImageView homeIcon      = root.findViewById(R.id.menu_bar__Home_Icon);
        TextView  homeText      = root.findViewById(R.id.menu_bar__Home_Text);

        ImageView bookingSelect = root.findViewById(R.id.menu_bar__BookingIconText_Select);
        ImageView bookingIcon   = root.findViewById(R.id.menu_bar__Booking_Icon);
        TextView  bookingText   = root.findViewById(R.id.menu_bar_Booking_Text);

        ImageView profileSelect = root.findViewById(R.id.menu_bar___ProfileIconText_Select);
        ImageView profileIcon   = root.findViewById(R.id.menu_bar__Profile_Icon);
        TextView  profileText   = root.findViewById(R.id.menu_bar__Profile_Text);

        // --- Prepare colors ---
        int black = ContextCompat.getColor(activity, R.color.black);
        int blue  = ContextCompat.getColor(activity, R.color.blue);

        // --- Reset all items to inactive state ---
        homeSelect.setVisibility(View.INVISIBLE);
        bookingSelect.setVisibility(View.INVISIBLE);
        profileSelect.setVisibility(View.INVISIBLE);

        homeIcon.setColorFilter(black);
        bookingIcon.setColorFilter(black);
        profileIcon.setColorFilter(black);

        homeText.setTextColor(black);
        bookingText.setTextColor(black);
        profileText.setTextColor(black);

        // --- Highlight the active section ---
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