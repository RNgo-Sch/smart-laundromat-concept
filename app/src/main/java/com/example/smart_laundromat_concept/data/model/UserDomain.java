package com.example.smart_laundromat_concept.data.model;



import com.example.smart_laundromat_concept.data.model.User;

public class UserDomain {
    public User user;
    public Wallet wallet;
    public Reputation reputation;

    public UserDomain(User user) {
        this.user = user;
        this.wallet = new Wallet(user.getWallet());
        this.reputation = new Reputation(user.getReputation());
    }
}