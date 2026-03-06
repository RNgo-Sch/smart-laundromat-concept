package com.example.smart_laundromat_concept.classes;

public class TestUser {
    public static void main(String args[]) {
        User testUser1 = new User();
        User testUser2 = new User();

        // testing IdCounter is working
        System.out.println("testUser1: " + testUser1.getId() + "; testUser2: " + testUser2.getId());

        // testing Wallet
        Wallet objWallet = testUser2.getWallet();
        objWallet.topUp(10);
        System.out.println(objWallet);
        }
}