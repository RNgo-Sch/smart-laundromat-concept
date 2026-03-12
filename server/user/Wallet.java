package server.user;

// class exists to track the current state of user wallet
public class Wallet {
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
    public void debit(float amount) {
        balance -= amount;
        System.out.println("Debit amount $" + amount + " leaving balance $" + balance);
    }
    public boolean makePayment(float amount) {
        // method returns true if successful, false if failed due to insufficient funds
        if (balance < 0.0f) {
            System.out.println("Account cannot make payment: in debt");
            return false;
        } else {
            debit(amount);
            System.out.println("Sucsessful payment made");
            return true;
        }
    }

    // misc
    // override hashMap and equals 
    @Override
    public String toString() {
        return "Wallet balance: $" + balance;
    }
}