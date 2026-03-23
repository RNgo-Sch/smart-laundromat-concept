package com.example.smart_laundromat_concept.ui.common;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.smart_laundromat_concept.R;

/**
 * Utility class for configuring the reusable layout_button component.
 */
public class ButtonHelper {

    /**
     * Configures a reusable layout_button with specific text and a click listener.
     *
     * @param activity  the current Activity context
     * @param includeId the ID assigned to the include tag in the layout XML
     * @param label     the text to display on the button
     * @param listener  the action to perform when the button is clicked
     */
    public static void setup(Activity activity, int includeId, String label, View.OnClickListener listener) {
        View root = activity.findViewById(includeId);
        if (root == null) return;

        TextView textView = root.findViewById(R.id.layout_button__tv__text);
        if (textView != null) {
            textView.setText(label);
        }

        View interactiveArea = root.findViewById(R.id.layout_button__btn__button);
        if (interactiveArea != null) {
            interactiveArea.setOnClickListener(listener);
        }
    }
}