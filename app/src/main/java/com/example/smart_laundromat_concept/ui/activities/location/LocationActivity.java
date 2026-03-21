package com.example.smart_laundromat_concept.ui.activities.location;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.session.LocationSession;
import com.example.smart_laundromat_concept.ui.navigation.NavigationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * LocationActivity displays a Google Map with pins on all laundromat locations.
 * <p>
 * When the user taps a pin, a confirmation dialog appears asking if they want
 * to change their location. If confirmed, the selection is saved to
 * {@link LocationSession} and the activity closes.
 * <p>
 * Implements {@link OnMapReadyCallback} to receive the map when it is ready.
 */
public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float ZOOM = 17f;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.activity_location__map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // -------------------------------------------------------------------------
    // Map
    // -------------------------------------------------------------------------

    /**
     * Called when the Google Map is ready to use.
     * Places markers on all laundromat locations and sets up the pin tap listener.
     *
     * @param googleMap the ready-to-use GoogleMap instance
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // --- Pin 1: Blk 59 (main location) ---
        LatLng blk59 = new LatLng(1.3420039015877259, 103.96362141957202);
        googleMap.addMarker(new MarkerOptions()
                .position(blk59)
                .title("SUTD Blk 59"));

        // --- Pin 2: Blk 57 ---
        LatLng blk57 = new LatLng(1.34235605004437, 103.96393873191897);
        googleMap.addMarker(new MarkerOptions()
                .position(blk57)
                .title("SUTD Blk 57"));

        // --- Pin 3: Blk 55 ---
        LatLng blk55 = new LatLng(1.3426172273462624, 103.96418944925759);
        googleMap.addMarker(new MarkerOptions()
                .position(blk55)
                .title("SUTD Blk 55"));

        // Camera focuses on currently selected location
        LatLng current = new LatLng(
                LocationSession.getInstance().getLatitude(),
                LocationSession.getInstance().getLongitude()
        );
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, ZOOM));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // --- Handle pin tap — show confirmation dialog ---
        googleMap.setOnMarkerClickListener(marker -> {
            showLocationConfirmDialog(
                    marker.getTitle(),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude
            );
            return true;
        });
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Shows a confirmation dialog asking the user if they want to change
     * their location to the selected pin.
     *
     * @param name      the display name of the selected location
     * @param latitude  the latitude of the selected location
     * @param longitude the longitude of the selected location
     */
    private void showLocationConfirmDialog(String name, double latitude, double longitude) {
        new AlertDialog.Builder(this)
                .setTitle("Change location")
                .setMessage("Set location to " + name + "?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // Save to session and return to previous screen
                    LocationSession.getInstance().setLocation(name, latitude, longitude);
                    Toast.makeText(this, "Location set to " + name, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Dismiss dialog — user stays on map to pick another pin
                    dialog.dismiss();
                })
                .show();
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    /**
     * Delegates page navigation to the centralized NavigationHelper class.
     */
    public void launchPage(View view) {
        NavigationHelper.launchPage(this, view);
    }
}