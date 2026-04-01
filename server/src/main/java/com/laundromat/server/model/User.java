package com.laundromat.server.model;

public class User {

    private Integer id;
    public final Wallet wallet;
    public final Reputation reputation;

    public String username;
    public String password;

    public User(int id, String username, String password, float wallet, int reputation) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.wallet = new Wallet(wallet);
        this.reputation = new Reputation(reputation);
    }

    // accessors
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof User other) return this.id.equals(other.id);
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    // Helper Classes
    public static class Wallet {
        float balance;

        public Wallet() {
            this.balance = 0.0f;
        }

        public Wallet(float initialBalance) {
            this.balance = initialBalance;
        }

        public float getBalance() {
            return balance;
        }

        public void topUp(float amount) {
            balance += amount;
            System.out.println("Topped up $" + amount + " to $" + balance);
        }

        public boolean makePayment(float amount) {
            if (balance < 0.0f) {
                System.out.println("Account cannot make payment: in debt");
                return false;
            } else {
                topUp(-amount);
                System.out.println("Successful payment made");
                return true;
            }
        }

        @Override
        public String toString() {
            return "Wallet balance: $" + balance;
        }
    }

    public static class Reputation {
        private static final int MIN_SCORE = -10;
        private static final int MAX_SCORE = 120;
        private static final int[] TIERS = { 10, 25, 50, 100 };

        private int score;

        public Reputation() {
            this.score = 0;
        }

        public Reputation(int initialScore) {
            this.score = initialScore;
        }

        public int getReputationTier() {
            for (int i = 0; i < TIERS.length; i++) {
                if (score < TIERS[i]) return i;
            }
            return TIERS.length;
        }

        public void adjustScore(int adjustment) {
            if ((score + adjustment >= MIN_SCORE) && (score + adjustment <= MAX_SCORE)) {
                score += adjustment;
            }
        }

        @Override
        public String toString() {
            return "Reputation tier: " + this.getReputationTier();
        }
    }
}