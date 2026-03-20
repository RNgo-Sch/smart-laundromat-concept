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
 * <p>
 * - Updating button styles (colors and tints).
 * <p>
 * - Triggering UI updates via the machine managers.
 * <p>
 * - Coordinating slide animations between layout containers.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on {@link NavigationHelper#executeInternalTransition}
 * to see how the view sliding logic is implemented.
 */
public class BookingNavigator implements NavigatorModule {

    /**
     * Processes booking-related navigation requests based on view IDs.
     *
     * @param activity The current Activity context.
     * @param id The ID of the clicked View.
     * @return A {@link NavigationRequest} if the ID is handled, otherwise null.
     */
    @Override
    public NavigationRequest handle(Activity activity, int id) {
        // --- 1. Washer View Selection ---
        if (id == R.id.activity_booking__btn__washer) {
            return updateUI(activity, true);
        }

        // --- 2. Dryer View Selection ---
        if (id == R.id.activity_booking__btn__dryer) {
            return updateUI(activity, false);
        }

        return null;
    }

    /**
     * Updates the UI state and returns a NavigationRequest for the helper to animate.
     * <p>
     * This method synchronizes the visual state of the selection buttons with the 
     * visibility of the machine containers.
     *
     * @param activity Current activity.
     * @param showWasher True if the Washer section should be shown, false for Dryer.
     * @return A {@link NavigationRequest} describing the internal transition.
     */
    private NavigationRequest updateUI(Activity activity, boolean showWasher) {
        Button washerButton = activity.findViewById(R.id.activity_booking__btn__washer);
        Button dryerButton = activity.findViewById(R.id.activity_booking__btn__dryer);
        View washerContainer = activity.findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer = activity.findViewById(R.id.activity_booking__view__dryer_container);

        if (washerButton == null || dryerButton == null || washerContainer == null || dryerContainer == null) {
            return null;
        }

        // --- UI STYLE UPDATES ---
        int white = ContextCompat.getColor(activity, R.color.white);
        int black = ContextCompat.getColor(activity, R.color.black);
        int blue = ContextCompat.getColor(activity, R.color.blue);

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

        // --- DATA SYNC ---
        // Force managers to refresh their respective views
        if (activity instanceof BookingActivity) {
            BookingActivity ba = (BookingActivity) activity;
            if (showWasher) ba.getWasherManager().updateAll();
            else ba.getDryerManager().updateAll();
        }

        // --- ANIMATION REQUEST ---
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
