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
 * Handles internal UI state navigation within the BookingActivity.
 * <p>
 * This navigator manages the toggling between Washer and Dryer views, including:
 * <ul>
 *   <li>Updating button styles (colors and tints).</li>
 *   <li>Triggering UI updates via the machine managers.</li>
 *   <li>Coordinating slide animations between layout containers.</li>
 * </ul>
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link NavigationHelper#executeInternalTransition}
 * to see how the view sliding logic is implemented.
 */
public class BookingNavigator implements NavigatorModule {

    // -------------------------------------------------------------------------
    // NavigatorModule
    // -------------------------------------------------------------------------

    /**
     * Processes booking-related navigation requests based on view IDs.
     *
     * @param activity the current Activity context
     * @param id       the ID of the clicked View
     * @return a {@link NavigationRequest} if the ID is handled, otherwise null
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {
        if (id == R.id.activity_booking__btn__washer) {
            return updateUI(activity, true);
        }

        if (id == R.id.activity_booking__btn__dryer) {
            return updateUI(activity, false);
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Synchronizes button styles, machine data, and container visibility
     * based on the selected machine type.
     *
     * @param activity    the current Activity context
     * @param showWasher  true to show the Washer section, false for Dryer
     * @return a {@link NavigationRequest} describing the internal transition, or null
     */
    private NavigationRequest updateUI(Activity activity, boolean showWasher) {
        Button washerButton     = activity.findViewById(R.id.activity_booking__btn__washer);
        Button dryerButton      = activity.findViewById(R.id.activity_booking__btn__dryer);
        View washerContainer    = activity.findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer     = activity.findViewById(R.id.activity_booking__view__dryer_container);

        if (washerButton == null || dryerButton == null || washerContainer == null || dryerContainer == null) {
            return null;
        }

        int white = ContextCompat.getColor(activity, R.color.white);
        int black = ContextCompat.getColor(activity, R.color.black);
        int blue  = ContextCompat.getColor(activity, R.color.blue);

        // --- Update button styles ---
        if (showWasher) {
            washerButton.setBackgroundTintList(ColorStateList.valueOf(blue));
            washerButton.setTextColor(white);
            TextViewCompat.setCompoundDrawableTintList(washerButton, ColorStateList.valueOf(white));

            dryerButton.setBackgroundTintList(null);
            dryerButton.setTextColor(black);
            TextViewCompat.setCompoundDrawableTintList(dryerButton, ColorStateList.valueOf(black));
        } else {
            washerButton.setBackgroundTintList(null);
            washerButton.setTextColor(black);
            TextViewCompat.setCompoundDrawableTintList(washerButton, ColorStateList.valueOf(black));

            dryerButton.setBackgroundTintList(ColorStateList.valueOf(blue));
            dryerButton.setTextColor(white);
            TextViewCompat.setCompoundDrawableTintList(dryerButton, ColorStateList.valueOf(white));
        }

        // --- Sync machine data with managers ---
        if (activity instanceof BookingActivity) {
            BookingActivity bookingActivity = (BookingActivity) activity;
            if (showWasher) {
                bookingActivity.getWasherManager().updateAll();
            } else {
                bookingActivity.getDryerManager().updateAll();
            }
        }

        // --- Return animation request based on current visibility ---
        if (showWasher) {
            if (dryerContainer.getVisibility() == View.VISIBLE) {
                return new NavigationRequest(washerContainer, dryerContainer, NavigationRequest.AnimationType.INTERNAL_SLIDE_LEFT);
            } else {
                washerContainer.setVisibility(View.VISIBLE);
                dryerContainer.setVisibility(View.GONE);
            }
        } else {
            if (washerContainer.getVisibility() == View.VISIBLE) {
                return new NavigationRequest(dryerContainer, washerContainer, NavigationRequest.AnimationType.INTERNAL_SLIDE_RIGHT);
            } else {
                dryerContainer.setVisibility(View.VISIBLE);
                washerContainer.setVisibility(View.GONE);
            }
        }

        return null;
    }
}