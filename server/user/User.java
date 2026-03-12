package server.user;

import server.idcounter.IdCounter;

public class User {
    // static variables for id and reputation system
    private static final int[] REPUTATION_TIERS = {10, 20, 30, 40}; // breakpoints to enter reputation level 1, 2, 3, 4
    private static IdCounter latestId = new IdCounter(); // most recent id value used

    private final int id;
    private Wallet wallet;
    private Reputation reputation;

    private String username;
    private String password;

    public User() {
        this.id = latestId.getId();
        this.wallet = new Wallet();
        this.reputation = new Reputation();
        this.username = String.valueOf(this.id);
        this.password = "1";
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
    public Wallet getWallet() {
        return wallet;
    }
    public Reputation getReputation() {
        return reputation;
    }

    @Override
    public String toString() {
        return "User id " + id + "\n\t" + reputation + "\n\t" + wallet; 
    }
    public static String getInfo() {
        return "Users registered : " + latestId; 
    }
}
