package com.example.smart_laundromat_concept.data.session;

import com.example.smart_laundromat_concept.data.model.User;
import com.example.smart_laundromat_concept.data.remote.UserRepository;

import retrofit2.Callback;

/**
 * Singleton class to manage the current user session.
 * Stores the logged-in user and active booking state
 * so it can be accessed from any screen.
 */
public class UserSession {

    private static UserSession instance;

    // --- Current User ---
    private User currentUser;

    // --- Active Booking ---
    private String activeMachineType;
    private int activeMachineNum;
    private long bookingEndTimeMillis;

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

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /** Returns username or null if not logged in. */
    public String getUsername() {
        return currentUser != null ? currentUser.username : null;
    }

    /** Returns wallet balance or 0 if not logged in. */
    public float getWallet() {
        return currentUser != null ? currentUser.getWallet() : 0f;
    }

    public void topUpAndSync(float amount) {
        topUpAndSync(amount, null);
    }

    public void topUpAndSync(float amount, Runnable onComplete) {
        if (currentUser != null) {

            // 1. Update locally
            float newBalance = currentUser.getWallet() + amount;
            currentUser.setWallet(newBalance);

            // 2. Sync with Supabase
            UserRepository.updateWallet(
                    currentUser.getId(),
                    newBalance,
                    new Callback<Void>() {
                        @Override
                        public void onResponse(retrofit2.Call<Void> call,
                                               retrofit2.Response<Void> response) {
                            // ✅ success (optional: log or toast)
                            System.out.println("Wallet updated successfully");
                            if (onComplete != null) {
                                onComplete.run();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                            t.printStackTrace();
                            if (onComplete != null) {
                                onComplete.run();
                            }
                        }
                    }
            );
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

    /** Clears the current session, logging out the user and clearing any active booking. */
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

    public String getActiveMachineType()  { return activeMachineType; }
    public int    getActiveMachineNum()   { return activeMachineNum; }
    public long   getBookingEndTimeMillis() { return bookingEndTimeMillis; }
}
