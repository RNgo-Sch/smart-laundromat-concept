package com.example.smart_laundromat_concept.data.model;

import com.google.gson.annotations.SerializedName;

public class User {
    // static variables for id and reputation system
    private static final int[] REPUTATION_TIERS = {10, 20, 30, 40}; // breakpoints to enter reputation level 1, 2, 3, 4
    private static IdCounter latest_id = new IdCounter(); // most recent id value used

    private Integer id;
    //private Wallet wallet2;
    //private Reputation reputation;
    private float wallet;
    private int reputation;

    private String username;
    private String password;
    @SerializedName("phone_no")
    private String phoneNo;
    private float debt;

    public User() {
        this.id = null;
        this.wallet = 0;
        this.reputation = 0;
        this.username = String.valueOf(this.id);
        this.password = "1";
        this.phoneNo = "12345";
        this.debt = 0;
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    // accessors
    public int getId() {
        return id;
    }

    /*public Wallet getWallet() {
        return wallet2;
    }

     */



    /*public Reputation getReputation() {
        return reputation;
    }

     */
}
