package com.example.smart_laundromat_concept.ui.common;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.smart_laundromat_concept.R;

/**
 * Utility class for configuring the reusable {@code layout_button} component.
 *
 * <p>The reusable button layout consists of a {@link TextView} for the label
 * and a clickable {@link View} for the interactive area. This helper wires both
 * up in one call so Activities don't need to repeat this boilerplate.
 */
public class ButtonHelper {

    /**
     * Configures a {@code layout_button} include with a label and click listener.
     *
     * @param activity  the Activity that contains the button include
     * @param includeId the resource ID assigned to the {@code <include>} tag in XML
     * @param label     the text to display on the button
     * @param listener  the action to perform when the button is tapped
     */
    public static void setup(Activity activity, int includeId, String label,
                             View.OnClickListener listener) {
        View root = activity.findViewById(includeId);
        if (root == null) return;

        // Set button label text
        TextView textView = root.findViewById(R.id.layout_button__tv__text);
        if (textView != null) {
            textView.setText(label);
        }

        // Set click listener on the interactive area
        View interactiveArea = root.findViewById(R.id.layout_button__btn__button);
        if (interactiveArea != null) {
            interactiveArea.setOnClickListener(listener);
        }
    }
}