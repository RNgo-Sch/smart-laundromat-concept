package com.example.smart_laundromat_concept.data.model;

/**
 * Represents a user in the Smart Laundromat system.
 * <p>
 * This is a data model (POJO) used for:
 * <ul>
 *     <li>Storing user information locally in the app</li>
 *     <li>Mapping JSON data from Supabase via Retrofit/Gson</li>
 * </ul>
 *
 * This class does NOT contain business logic. It only holds data.
 */
public class User {

    /** Unique user ID assigned by Supabase (primary key). */
    private Integer id;

    /** Current wallet balance of the user. */
    private float wallet;

    /** Reputation score used for tier calculation. */
    private int reputation;

    /** Username used for login. */
    private String username;

    /** Password used for authentication (plain text for now). */
    private String password;

    /**
     * Default constructor required for Gson deserialization.
     * Initializes fields with safe default values.
     */
    public User() {
        this.id = null;
        this.wallet = 0f;
        this.reputation = 0;
        this.username = "";
        this.password = "";
    }

    /**
     * Creates a new user with the given username and password.
     * Used during signup or login.
     *
     * @param username the user's username
     * @param password the user's password
     */
    public User(String username, String password) {
        this.id = null;
        this.wallet = 0f;
        this.reputation = 0;
        this.username = username;
        this.password = password;
    }

    /** @return the user's unique ID */
    public Integer getId() { return id; }

    /** @return the user's wallet balance */
    public float getWallet() { return wallet; }

    /** @return the user's reputation score */
    public int getReputation() { return reputation; }

    /** @return the user's username */
    public String getUsername() { return username; }

    /** @return the user's password */
    public String getPassword() { return password; }

    /**
     * Updates the user's wallet balance.
     * @param wallet new wallet balance
     */
    public void setWallet(float wallet) { this.wallet = wallet; }

    /**
     * Updates the user's reputation score.
     * @param reputation new reputation value
     */
    public void setReputation(int reputation) { this.reputation = reputation; }
}