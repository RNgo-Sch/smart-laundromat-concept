package com.example.smart_laundromat_concept.ui.navigation;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.example.smart_laundromat_concept.R;

/**
 * Handles internal UI state navigation within the BookingActivity.
 * Since this only changes visibility/content of the same screen, it returns null
 * as no Activity-level Intent is needed.
 */
public class BookingNavigator implements NavigatorModule {

    @Override
    public NavigationRequest handle(Activity activity, int id) {
        
        // --- 1. Washer View Selection ---
        if (id == R.id.activity_booking__btn__washer) {
            updateUI(activity, true);
            return null; // No Activity navigation
        }

        // --- 2. Dryer View Selection ---
        if (id == R.id.activity_booking__btn__dryer) {
            updateUI(activity, false);
            return null; // No Activity navigation
        }

        return null;
    }

    /**
     * Updates the UI state of the BookingActivity to toggle between Washer and Dryer views.
     * This method handles both container visibility and button styling (background, text, and icons).
     */
    private void updateUI(Activity activity, boolean showWasher) {
        // 1. Reference all required UI elements
        View washerContainer = activity.findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer = activity.findViewById(R.id.activity_booking__view__dryer_container);
        Button washerButton = activity.findViewById(R.id.activity_booking__btn__washer);
        Button dryerButton = activity.findViewById(R.id.activity_booking__btn__dryer);

        // 2. Safety Check: Ensure all views exist before proceeding
        if (washerContainer == null || dryerContainer == null || washerButton == null || dryerButton == null) {
            return;
        }

        // 3. Prepare Theme Colors
        int white = ContextCompat.getColor(activity, R.color.white);
        int black = ContextCompat.getColor(activity, R.color.black);
        int blue = ContextCompat.getColor(activity, R.color.blue);

        // 4. Execute Combined UI Toggle
        if (showWasher) {
            // --- STATE: Washer Selected ---
            washerContainer.setVisibility(View.VISIBLE);
            dryerContainer.setVisibility(View.GONE);

            // Highlight Washer Button
            washerButton.setBackgroundTintList(ColorStateList.valueOf(blue));
            washerButton.setTextColor(white);
            TextViewCompat.setCompoundDrawableTintList(washerButton, ColorStateList.valueOf(white));

            // Reset Dryer Button
            dryerButton.setBackgroundTintList(ColorStateList.valueOf(white));
            dryerButton.setTextColor(black);
            TextViewCompat.setCompoundDrawableTintList(dryerButton, ColorStateList.valueOf(black));
        } else {
            // --- STATE: Dryer Selected ---
            washerContainer.setVisibility(View.GONE);
            dryerContainer.setVisibility(View.VISIBLE);

            // Reset Washer Button
            washerButton.setBackgroundTintList(ColorStateList.valueOf(white));
            washerButton.setTextColor(black);
            TextViewCompat.setCompoundDrawableTintList(washerButton, ColorStateList.valueOf(black));

            // Highlight Dryer Button
            dryerButton.setBackgroundTintList(ColorStateList.valueOf(blue));
            dryerButton.setTextColor(white);
            TextViewCompat.setCompoundDrawableTintList(dryerButton, ColorStateList.valueOf(white));
        }
    }
}
