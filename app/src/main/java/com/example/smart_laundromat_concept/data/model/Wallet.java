package com.example.smart_laundromat_concept.data.model;


public class Wallet {
    private float balance;

    public Wallet(float balance) {
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public void topUp(float amount) {
        balance += amount;
    }

    public boolean makePayment(float amount) {
        if (balance < amount) return false;
        balance -= amount;
        return true;
    }
}