package com.example.smart_laundromat_concept.ui.common;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.example.smart_laundromat_concept.R;

/**
 * Utility class to configure the reusable layout_button across different activities.
 */
public class ButtonHelper {

    /**
     * Configures a reusable layout_button with specific text and a click listener.
     * 
     * @param activity   The current Activity context.
     * @param includeId  The ID assigned to the <include> tag in the layout XML.
     * @param label      The text to display on the button.
     * @param listener   The action to perform when the button is clicked.
     */
    public static void setup(Activity activity, int includeId, String label, View.OnClickListener listener) {
        View root = activity.findViewById(includeId);
        if (root != null) {
            // 1. Set the Label Text
            TextView textView = root.findViewById(R.id.layout_button__tv__text);
            if (textView != null) {
                textView.setText(label);
            }

            // 2. Set the Click Listener on the interactive area
            View interactiveArea = root.findViewById(R.id.layout_button__btn__button);
            if (interactiveArea != null) {
                interactiveArea.setOnClickListener(listener);
            }
        }
    }
}
