package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.main.booking.BookingActivity;

/**
 * Handles the internal tab switch between the Washer and Dryer views
 * inside {@link BookingActivity}.
 *
 * <p>This is not a screen-to-screen navigation — it is an <b>internal UI transition</b>
 * that slides the machine container views left or right and updates the tab button styles.
 *
 * <p><b>OOP Design — Polymorphism:</b><br>
 * Implements {@link NavigatorModule} so it integrates cleanly into
 * {@link NavigationHelper}'s module chain, just like all other navigators.
 */
public class BookingNavigator implements NavigatorModule {

    // -------------------------------------------------------------------------
    // NavigatorModule
    // -------------------------------------------------------------------------

    /**
     * Handles a tap on either the Washer or Dryer tab button.
     *
     * @param activity the current Activity context
     * @param id       the resource ID of the clicked view
     * @return a {@link NavigationRequest} for the internal slide animation, or null
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {
        if (id == R.id.activity_booking__btn__washer) {
            return switchTab(activity, true);
        }
        if (id == R.id.activity_booking__btn__dryer) {
            return switchTab(activity, false);
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Applies button style changes and prepares the internal slide animation
     * when switching between the Washer and Dryer tabs.
     *
     * @param activity   the current Activity context
     * @param showWasher true to activate the Washer tab, false for the Dryer tab
     * @return a {@link NavigationRequest} for the slide animation, or null
     */
    private NavigationRequest switchTab(Activity activity, boolean showWasher) {
        Button washerButton  = activity.findViewById(R.id.activity_booking__btn__washer);
        Button dryerButton   = activity.findViewById(R.id.activity_booking__btn__dryer);
        View washerContainer = activity.findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer  = activity.findViewById(R.id.activity_booking__view__dryer_container);

        if (washerButton == null || dryerButton == null
                || washerContainer == null || dryerContainer == null) {
            return null;
        }

        int white = ContextCompat.getColor(activity, R.color.white);
        int black = ContextCompat.getColor(activity, R.color.black);
        int blue  = ContextCompat.getColor(activity, R.color.blue);

        // Update tab button styles: active = blue fill, inactive = transparent
        if (showWasher) {
            applyActiveStyle(washerButton, blue, white, activity);
            applyInactiveStyle(dryerButton, black);
            NavigationHelper.fadeInAlpha(washerButton);
        } else {
            applyInactiveStyle(washerButton, black);
            applyActiveStyle(dryerButton, blue, white, activity);
            NavigationHelper.fadeInAlpha(dryerButton);
        }

        // Notify BookingActivity so its machine managers can refresh their data
        if (activity instanceof BookingActivity) {
            BookingActivity bookingActivity = (BookingActivity) activity;
            if (showWasher) bookingActivity.getWasherManager().updateAll();
            else            bookingActivity.getDryerManager().updateAll();
        }

        // Return the slide animation request based on which container is currently visible
        if (showWasher) {
            if (dryerContainer.getVisibility() == View.VISIBLE) {
                return new NavigationRequest(washerContainer, dryerContainer,
                        NavigationRequest.AnimationType.INTERNAL_SLIDE_LEFT);
            }
        } else {
            if (washerContainer.getVisibility() == View.VISIBLE) {
                return new NavigationRequest(dryerContainer, washerContainer,
                        NavigationRequest.AnimationType.INTERNAL_SLIDE_RIGHT);
            }
        }

        // Fallback — set visibility directly if no slide animation is needed
        washerContainer.setVisibility(showWasher ? View.VISIBLE : View.GONE);
        dryerContainer.setVisibility(showWasher ? View.GONE : View.VISIBLE);
        return null;
    }

    /** Applies the active (filled blue) style to a tab button. */
    private void applyActiveStyle(Button button, int blue, int white, Activity activity) {
        button.setBackgroundTintList(ColorStateList.valueOf(blue));
        button.setTextColor(white);
        TextViewCompat.setCompoundDrawableTintList(button, ColorStateList.valueOf(white));
    }

    /** Applies the inactive (no fill) style to a tab button. */
    private void applyInactiveStyle(Button button, int black) {
        button.setBackgroundTintList(null);
        button.setTextColor(black);
        TextViewCompat.setCompoundDrawableTintList(button, ColorStateList.valueOf(black));
    }
}