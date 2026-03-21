package com.example.smart_laundromat_concept.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a user account in the system.
 * Fields are mapped to the Supabase 'users' table via Gson.
 */
public class User {

    // --- Fields ---
    private Integer id;
    private float wallet;
    private int reputation;

    public String username;
    public String password;

    @SerializedName("phone_no")
    private String phoneNo;

    private float debt;


    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Default constructor. Initialises all fields to safe empty values.
     * Used by Gson when deserialising a user from Supabase.
     */
    public User() {
        this.id = null;
        this.wallet = 0;
        this.reputation = 0;
        this.username = "";
        this.password = "";
        this.phoneNo = "";
        this.debt = 0;
    }

    /**
     * Creates a user with a username and password.
     * Used when registering or logging in.
     *
     * @param username the user's chosen username
     * @param password the user's chosen password
     */
    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }


    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    /**
     * Returns the user's unique ID assigned by Supabase.
     * May be null if the user has not been saved to the database yet.
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
}