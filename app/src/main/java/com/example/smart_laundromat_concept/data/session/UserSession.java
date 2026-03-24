package com.example.smart_laundromat_concept.data.session;

import com.example.smart_laundromat_concept.data.model.User;
import com.example.smart_laundromat_concept.data.remote.UserRepository;

import retrofit2.Callback;

/**
 * Singleton class that manages the current user's session.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Stores the currently logged-in user</li>
 *     <li>Provides easy access to user data (username, wallet, reputation)</li>
 *     <li>Maintains temporary app state such as active bookings</li>
 * </ul>
 *
 * This class does NOT perform network/API calls. It only manages local state.
 */
public class UserSession {

    private static UserSession instance;

    // --- Current User ---
    /** The currently logged-in user (null if not logged in). */
    private User currentUser;

    // --- Active Booking ---
    /** Type of machine currently booked (e.g., Washer or Dryer). */
    private String activeMachineType;

    /** Machine number currently booked. */
    private int activeMachineNum;

    /** Timestamp (in milliseconds) when the booking ends. */
    private long bookingEndTimeMillis;

    private UserSession() {}

    /**
     * Returns the single instance of UserSession (Singleton pattern).
     * Creates one if it does not exist.
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

// -------------------------------------------------------------------------
// Auth
// -------------------------------------------------------------------------

    /**
     * Sets the currently logged-in user.
     * Called after successful login.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * @return the current logged-in user object
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /** Returns username or null if not logged in. */
    public String getUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    /** Returns wallet balance or 0 if not logged in. */
    public float getWallet() {
        return currentUser != null ? currentUser.getWallet() : 0f;
    }

    /**
     * Increases the user's wallet balance locally.
     * <p>
     * NOTE: This does NOT sync with the backend.
     * API calls should be handled separately in the Activity/Repository.
     *
     * @param amount amount to add to wallet
     */
    public void topUp(float amount) {
        if (currentUser != null) {
            float newBalance = currentUser.getWallet() + amount;
            currentUser.setWallet(newBalance);
        }
    }


    /** Returns reputation score or 0 if not logged in. */
    public int getReputation() {
        return currentUser != null ? currentUser.getReputation() : 0;
    }

    /** Returns true if a user is currently logged in. */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Logs out the current user.
     * Clears user data and active booking state.
     */
    public void logout() {
        this.currentUser = null;
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
     * @param machineType    "Washer" or "Dryer"
     * @param machineNum     1-based machine number
     * @param durationMillis how long the cycle runs in milliseconds
     *                       e.g. 30 minutes = 30 * 60 * 1000
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

    /** @return the type of machine currently booked */
    public String getActiveMachineType()  { return activeMachineType; }

    /** @return the machine number currently booked */
    public int    getActiveMachineNum()   { return activeMachineNum; }

    /** @return booking end time in milliseconds */
    public long   getBookingEndTimeMillis() { return bookingEndTimeMillis; }
}
