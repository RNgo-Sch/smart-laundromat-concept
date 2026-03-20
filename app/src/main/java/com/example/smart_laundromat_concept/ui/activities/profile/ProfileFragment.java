package com.example.smart_laundromat_concept.ui.activities.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.session.UserSession;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

/**
 * ProfileFragment displays the user profile screen.
 * <p>
 * It is hosted within {@link com.example.smart_laundromat_concept.ui.activities.main.MainActivity}
 * to allow for stationary menu bar navigation.
 */
public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide local menu bar if present (Host Activity handles it)
        View localMenu = view.findViewById(R.id.menu_bar);
        if (localMenu != null) localMenu.setVisibility(View.GONE);

        loadUserData(view);

        // Setup generic click listener for child buttons
        setupNavigation(view);
    }

    private void loadUserData(View root) {
        String username = UserSession.getInstance().getUsername();
        if (username != null && !username.isEmpty()) {
            TextView tvUsernameTitle = root.findViewById(R.id.activity_profile__username_title);
            TextView tvLetterCircle = root.findViewById(R.id.activity_profile__letter_circle);
            
            if (tvUsernameTitle != null) tvUsernameTitle.setText(username);
            if (tvLetterCircle != null) {
                String firstLetter = username.substring(0, 1).toUpperCase();
                tvLetterCircle.setText(firstLetter);
            }
        }
    }

    private void setupNavigation(View root) {
        // Find clickable elements and attach the NavigationHelper
        // You might need to add IDs to the buttons in layout_profile if they aren't standard
        View logoutBtn = root.findViewById(R.id.activity_profile__Logout_Button);
        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> NavigationHelper.launchPage(requireActivity(), v));
        }
    }
}
