package com.example.smart_laundromat_concept.data.session;

/**
 * Singleton class to manage the current user session.
 * Stores the logged-in user's information and active booking state
 * so it can be accessed from any screen.
 */
public class UserSession {
    private static UserSession instance;

    // --- Auth ---
    private String username;

    // --- Active Booking ---
    private String activeMachineType;   // e.g. "Washer" or "Dryer"
    private int    activeMachineNum;    // e.g. 2
    private long   bookingEndTimeMillis; // System.currentTimeMillis() + duration

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // Auth
    // -------------------------------------------------------------------------

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void logout() {
        this.username = null;
        clearActiveBooking();
    }

    // -------------------------------------------------------------------------
    // Active Booking
    // -------------------------------------------------------------------------

    /**
     * Returns true if the user currently has a booking whose end time is in the future.
     */
    public boolean hasActiveBooking() {
        return bookingEndTimeMillis > System.currentTimeMillis();
    }

    /**
     * Stores a new active booking in the session.
     *
     * @param machineType       "Washer" or "Dryer"
     * @param machineNum        1-based machine number
     * @param durationMillis    How long the cycle runs, in milliseconds
     *                          e.g. 30 minutes = 30 * 60 * 1000
     */
    public void setActiveBooking(String machineType, int machineNum, long durationMillis) {
        this.activeMachineType    = machineType;
        this.activeMachineNum     = machineNum;
        this.bookingEndTimeMillis = System.currentTimeMillis() + durationMillis;
    }

    /**
     * Clears the active booking (called when the timer hits zero or user logs out).
     */
    public void clearActiveBooking() {
        this.activeMachineType    = null;
        this.activeMachineNum     = 0;
        this.bookingEndTimeMillis = 0;
    }

    public String getActiveMachineType() {
        return activeMachineType;
    }

    public int getActiveMachineNum() {
        return activeMachineNum;
    }

    public long getBookingEndTimeMillis() {
        return bookingEndTimeMillis;
    }
}
