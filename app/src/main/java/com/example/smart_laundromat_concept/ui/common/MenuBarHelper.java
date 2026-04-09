package com.example.smart_laundromat_concept.ui.common;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.smart_laundromat_concept.R;

/**
 * Utility class that highlights the active item in the shared bottom menu bar.
 *
 * <p>The menu bar appears on the Home, Booking, and Profile screens. This helper
 * resets all three items to their inactive (black) state and then highlights
 * the one matching the current screen in blue.
 *
 * <p>Usage — call once in each Activity's {@code onCreate()}:
 * <pre>
 *   MenuBarHelper.menuBar(this, MenuBarHelper.HOME);
 * </pre>
 */
public class MenuBarHelper {

    // -------------------------------------------------------------------------
    // Constants — identify which menu item to highlight
    // -------------------------------------------------------------------------

    /** Pass this to highlight the Home menu item. */
    public static final int HOME    = 0;

    /** Pass this to highlight the Booking menu item. */
    public static final int BOOKING = 1;

    /** Pass this to highlight the Profile menu item. */
    public static final int PROFILE = 2;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Updates the bottom menu bar to highlight the given section.
     *
     * <p>All items are first reset to their inactive (black) state, then
     * the active item's icon, text, and selection indicator are tinted blue.
     *
     * @param activity the Activity whose menu bar should be updated
     * @param menu     the section to highlight ({@link #HOME}, {@link #BOOKING},
     *                 or {@link #PROFILE})
     */
    public static void menuBar(Activity activity, int menu) {
        View root = activity.findViewById(android.R.id.content);

        // Resolve all menu item views
        ImageView homeSelect    = root.findViewById(R.id.menu_bar__HomeIconText_Select);
        ImageView homeIcon      = root.findViewById(R.id.menu_bar__Home_Icon);
        TextView  homeText      = root.findViewById(R.id.menu_bar__Home_Text);

        ImageView bookingSelect = root.findViewById(R.id.menu_bar__BookingIconText_Select);
        ImageView bookingIcon   = root.findViewById(R.id.menu_bar__Booking_Icon);
        TextView  bookingText   = root.findViewById(R.id.menu_bar_Booking_Text);

        ImageView profileSelect = root.findViewById(R.id.menu_bar___ProfileIconText_Select);
        ImageView profileIcon   = root.findViewById(R.id.menu_bar__Profile_Icon);
        TextView  profileText   = root.findViewById(R.id.menu_bar__Profile_Text);

        int black = ContextCompat.getColor(activity, R.color.black);
        int blue  = ContextCompat.getColor(activity, R.color.blue);

        // Reset all items to inactive state
        homeSelect.setVisibility(View.INVISIBLE);
        bookingSelect.setVisibility(View.INVISIBLE);
        profileSelect.setVisibility(View.INVISIBLE);

        homeIcon.setColorFilter(black);
        bookingIcon.setColorFilter(black);
        profileIcon.setColorFilter(black);

        homeText.setTextColor(black);
        bookingText.setTextColor(black);
        profileText.setTextColor(black);

        // Highlight the active section
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