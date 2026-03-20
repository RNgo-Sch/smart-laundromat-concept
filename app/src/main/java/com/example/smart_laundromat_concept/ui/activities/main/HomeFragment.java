package com.example.smart_laundromat_concept.ui.activities.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

/**
 * HomeFragment displays the primary dashboard content.
 * <p>
 * It is hosted within {@link MainActivity} to allow for stationary menu bar navigation.
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Use the existing activity_home layout but stripped of the MenuBar (handled by host)
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Remove the local menu bar if it exists in the layout (since Host handles it)
        View localMenu = view.findViewById(R.id.menu_bar);
        if (localMenu != null) localMenu.setVisibility(View.GONE);

        // Apply UI logic previously in HomeActivity
        LocationHelper.setupUnderline(requireActivity());

        // Set up click listeners for navigation
        view.findViewById(R.id.activity_home__wallet_top_up).setOnClickListener(v -> {
             NavigationHelper.launchPage(requireActivity(), v);
        });
    }
}
