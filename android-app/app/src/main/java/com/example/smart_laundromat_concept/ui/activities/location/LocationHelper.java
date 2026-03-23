package com.example.smart_laundromat_concept.ui.activities.location;

import android.app.Activity;
import android.graphics.Paint;
import android.widget.TextView;

import com.example.smart_laundromat_concept.R;

/**
 * UI Helper class for location-related view logic.
 * Centralizes styling and behavior for location components.
 * <p>
 * <b>Navigation Hint:</b> Hold Cmd/Ctrl + Click on any class or method reference
 * (e.g., {@link LocationHelper#setupUnderline}) to jump directly to its implementation.
 */
public class LocationHelper {


    /**
     * Finds the 'Change Location' text and applies a professional underline effect.
     * 
     * @param activity The Activity context where the location layout is inflated.
     */
    public static void setupUnderline(Activity activity) {
        // Find the specific TextView in the layout (Note: spelling "lication" matches XML ID)
        TextView tvChangeLocation = activity.findViewById(R.id.layout_location__change_location);
        
        if (tvChangeLocation != null) {
            // Apply the paint flag for underlining text
            tvChangeLocation.setPaintFlags(tvChangeLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
