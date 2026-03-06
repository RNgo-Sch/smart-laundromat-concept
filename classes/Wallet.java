// class exists to track the current state of user wallet
public class Wallet {
    float balance;
    float debt;
    
    public Wallet() {
        this.balance = 0.0f;
        this.debt = 0.0f;
    }

    // accessors
    public float getBalance() {
        return balance;
    }
    public float getDebt() {
        return balance;
    }

    // mutators
    public void topUp(float amount) {
        balance += amount;
    }
    public void increaseDebt(float amount) {
        debt += amount;
    }
    public boolean makePayment(float amount) {
        // method returns true if successful, false if failed due to insufficient funds
        // pay off debt in same payment
        amount += debt;
        debt = 0.0f;
        // make successful payment for amount with debt
        if (balance - amount < 0.0f) {
            System.out.println("Insufficient balance for payment: ");
            return false;
        } else {
            System.out.println("Sucsessful payment made.");
            balance -= amount;
            return true;
        }
    }

    // misc
    // override hashMap and equals 
    @Override
    public String toString() {
        return "balance: $" + balance + "debt: $" + debt;
    }
}