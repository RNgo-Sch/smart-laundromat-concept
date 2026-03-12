import server.user.*;
import server.facility.Facility;

public class Test {
    public static void main(String[] args) {
        testUsers();
        testFacility();
    }

    private static void testUsers() {
        System.out.println("Starting user test");

        // testing creating multiple users
        User u1 = new User();
        User u2 = new User();
        User u3 = new User();
        System.out.println(u1 + "\n" + u2 + "\n" + u3);
        System.out.println(User.getInfo());

        // Testing wallet functionality
        Wallet uWallet = u1.getWallet();
        System.out.println("User wallet before anything: " + uWallet);
        uWallet.topUp(298.0f);
        System.out.println("User wallet after topup: " + uWallet);
        boolean result = uWallet.makePayment(300.0f);
        System.out.println("Payment success: " + result);
        System.out.println("User wallet after deduction: " + uWallet);
        result = uWallet.makePayment(10.0f);
        System.out.println("Payment success: " + result);
        System.out.println("User wallet after deduction in debt: " + uWallet);

        // Testing reputation functionality
        Reputation uRep = u1.getReputation();
        System.out.println("User reputation before anything: " + uRep);
        uRep.adjustScore(10);
        System.out.println("User reputation after score up: " + uRep);
        uRep.adjustScore(100);
        System.out.println("User reputation after score up again: " + uRep);

        System.out.println("User test complete\n");
    }

    private static void testFacility() {
        System.out.println("Starting facility test");

        // Creating facility
        User u = new User();
        Facility f = new Facility();
        System.out.println(u + "" + f );
        
        // Queueing user with no reputation
        f.queueForFacility(u);
        System.out.println("Facility after queueing first user:\n" + f);

        // Queueing user with with reputation
        u = new User();
        u.getReputation().adjustScore(100);
        System.out.println("Facility after queueing second user:\n" + f);

        System.out.println("Facility test complete\n");
    }
}
