public class User {
    // static variables for id and reputation system
    private static final int[] REPUTATION_TIERS = {10, 20, 30, 40}; // breakpoints to enter reputation level 1, 2, 3, 4
    private static IdCounter latest_id = new IdCounter(); // most recent id value used

    private final int id;
    private Wallet wallet;
    private Reputation reputation;

    public User() {
        this.id = latest_id.getId();
        this.wallet = new Wallet();
        this.reputation = new Reputation();
    }

}