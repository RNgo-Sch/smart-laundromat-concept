package com.example.smart_laundromat_concept.data.model;

public class User {
    // static variables for id and reputation system
    private static IdCounter latest_id = new IdCounter(); // most recent id value used

    private int id;
    public final Wallet wallet;
    public final Reputation reputation;

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
        this.wallet = new Wallet();
        this.reputation = new Reputation();
        this.username = String.valueOf(this.id);
        this.password = "1";
    }

    /**
     * Creates a user with a username and password.
     * Used when registering or logging in.
     *
     * @param username the user's chosen username
     * @param password the user's chosen password
     */
    public User(String username, String password) {
    // -------------------------------------------------------------------------

    /**
     * Returns the user's unique ID assigned by Supabase.
     */
    public Integer getId() {
        return id;
    }

    // helper classes
    public static class Wallet {
        float balance;
        
        public Wallet() {
            this.balance = 0.0f;
        }

        // accessors
        public float getBalance() {
            return balance;
        }

        // mutators
        public void topUp(float amount) {
            balance += amount;
            System.out.println("Topped up $" + amount + " to $" + balance);
        }
        public boolean makePayment(float amount) {
            // method returns true if successful, false if failed due to insufficient funds
            if (balance < 0.0f) {
                System.out.println("Account cannot make payment: in debt");
                return false;
            } else {
                topUp(-amount);
                System.out.println("Sucsessful payment made");
                return true;
            }
        }

        // misc
        // TODO override equals and hashmap 
        @Override
        public String toString() {
            return "Wallet balance: $" + balance;
        }
    }
    public static class Reputation {
        private static final int MIN_SCORE = -10;
        private static final int MAX_SCORE = 120;
        private static final int[] TIERS = {
            10,
            25,
            50,
            100
        };
        
        private int score;
        
        public Reputation() {
            this.score = 0;
        }
        
        // accessors
        public int getReputationTier() {
            for (int i = 0; i < TIERS.length; i++) {
                if (score < TIERS[i]) {
                    return i;
                }
            }
            return TIERS.length;
        }

        // mutators
        public void adjustScore(int adjustment) {
            if ((score + adjustment >= MIN_SCORE) && (score + adjustment <= MAX_SCORE)) {
                score += adjustment;
            }
        }

        // misc
        public String toString() {
            return "Reputation tier: " + this.getReputationTier();
        }
    }
}
