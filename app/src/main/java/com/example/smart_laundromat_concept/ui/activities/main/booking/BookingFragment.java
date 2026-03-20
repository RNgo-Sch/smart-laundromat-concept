package com.example.smart_laundromat_concept.ui.activities.main.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.activities.location.LocationHelper;
import com.example.smart_laundromat_concept.ui.common.ButtonHelper;
import com.example.smart_laundromat_concept.ui.navigation.BookingNavigator;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;

/**
 * BookingFragment manages machine selection and status display.
 * <p>
 * It is hosted within {@link com.example.smart_laundromat_concept.ui.activities.main.MainActivity}
 * to allow for stationary menu bar navigation.
 */
public class BookingFragment extends Fragment {

    private WasherManager washerManager;
    private DryerManager dryerManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide local menu bar if present (Host Activity handles it)
        View localMenu = view.findViewById(R.id.menu_bar);
        if (localMenu != null) localMenu.setVisibility(View.GONE);

        // 1. Retrieve container views
        View washerContainer = view.findViewById(R.id.activity_booking__view__washer_container);
        View dryerContainer = view.findViewById(R.id.activity_booking__view__dryer_container);

        // 2. Initialize machine managers
        washerManager = new WasherManager(washerContainer);
        dryerManager = new DryerManager(dryerContainer);

        // 3. Setup UI styling
        LocationHelper.setupUnderline(requireActivity());

        // 4. Configure initial states
        setupInitialStates();

        // 5. Set default tab (Washer)
        new BookingNavigator().handle(requireActivity(), R.id.activity_booking__btn__washer);

        // 6. Setup Queue button
        ButtonHelper.setup(requireActivity(), R.id.activity_booking__btn__queue, "Join the Queue", v -> {
            Toast.makeText(getContext(), "Join the Queue!", Toast.LENGTH_SHORT).show();
        });
        
        // Setup individual machine click listeners
        setupMachineClickListeners(view);
    }

    private void setupInitialStates() {
        washerManager.setState(1, MachineStateHelper.STATE_RESERVED);
        washerManager.setState(2, MachineStateHelper.STATE_IN_USE);
        washerManager.setState(3, MachineStateHelper.STATE_OPEN);
        washerManager.setState(4, MachineStateHelper.STATE_OUT_OF_SERVICE);

        dryerManager.setState(1, MachineStateHelper.STATE_OPEN);
        dryerManager.setState(2, MachineStateHelper.STATE_COLLECTION);
        dryerManager.setState(3, MachineStateHelper.STATE_OUT_OF_SERVICE);
        dryerManager.setState(4, MachineStateHelper.STATE_IN_USE);

        washerManager.setOutline(1, true);
    }

    private void setupMachineClickListeners(View root) {
        // Find machine layouts and attach navigation
        int[] ids = {R.id.booking__machine_1, R.id.booking__machine_2, R.id.booking__machine_3, R.id.booking__machine_4};
        for (int id : ids) {
            View m = root.findViewById(id);
            if (m != null) m.setOnClickListener(v -> NavigationHelper.launchPage(requireActivity(), v));
        }
    }

    public WasherManager getWasherManager() { return washerManager; }
    public DryerManager getDryerManager() { return dryerManager; }
}
