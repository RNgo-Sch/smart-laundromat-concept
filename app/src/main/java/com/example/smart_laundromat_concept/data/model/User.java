package com.example.smart_laundromat_concept.data.model;

public class User {
    // static variables for id and reputation system
    private static IdCounter latest_id = new IdCounter(); // most recent id value used

    private Integer id;
    private float wallet;
    private int reputation;

    public String username;
    public String password;


    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Default constructor. Initialises all fields to safe empty values.
     * Used by Gson when deserialising a user from Supabase.
     */
    public User() {
        this.id = null;
        this.wallet = 0f;
        this.reputation = 0;
        this.username = "";
        this.password = "";
    }

    /**
     * Creates a user with a username and password.
     * Used when registering or logging in.
     *
     * @param username the user's chosen username
     * @param password the user's chosen password
     */
    public User(String username, String password) {
        this.id = null;
        this.wallet = 0f;
        this.reputation = 0;
        this.username = username;
        this.password = password;
    }
    // -------------------------------------------------------------------------

    /**
     * Returns the user's unique ID assigned by Supabase.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the user's current wallet balance.
     */
    public float getWallet() {
        return wallet;
    }

    /**
     * Returns the user's current reputation score.
     */
    public int getReputation() {
        return reputation;
    }

    /** Sets the user's wallet balance. */
    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    /** Sets the user's reputation score. */
    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

}
