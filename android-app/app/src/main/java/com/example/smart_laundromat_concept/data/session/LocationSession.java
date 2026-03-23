package com.example.smart_laundromat_concept.data.session;

/**
 * Singleton class to store the user's currently selected laundromat location.
 * Accessible from any screen via getInstance().
 */
public class LocationSession {

    private static LocationSession instance;

    private String locationName;
    private double latitude;
    private double longitude;

    private LocationSession() {
        // Default location — Blk 59
        this.locationName = "SUTD Blk 59";
        this.latitude     = 1.3420039015877259;
        this.longitude    = 103.96362141957202;
    }

    public static LocationSession getInstance() {
        if (instance == null) {
            instance = new LocationSession();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // -------------------------------------------------------------------------
    // Mutators
    // -------------------------------------------------------------------------

    /**
     * Updates the selected location.
     *
     * @param name      display name of the location
     * @param latitude  latitude coordinate
     * @param longitude longitude coordinate
     */
    public void setLocation(String name, double latitude, double longitude) {
        this.locationName = name;
        this.latitude     = latitude;
        this.longitude    = longitude;
    }
}