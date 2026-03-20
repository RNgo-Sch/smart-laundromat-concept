package com.example.smart_laundromat_concept.ui.activities.main;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.ui.common.MenuBarHelper;

/**
 * MainActivity serves as the Host Activity for the application's primary fragments.
 * <p>
 * It maintains a static MenuBar while allowing the content area to animate/slide.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_host);

        // Standard edge-to-edge padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_host__root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize with HomeFragment as default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_host__fragment_container, new HomeFragment())
                .commit();
        }

        // Initialize MenuBar (static)
        MenuBarHelper.menuBar(this, MenuBarHelper.HOME);
    }
}
