package com.example.smart_laundromat_concept.data.model;
public class Reputation {
    private static final int MIN_SCORE = -10;
    private static final int MAX_SCORE = 120;
    private static final int[] TIERS = {
        10,
        25,
        50,
        100
    };
    
    private int score;
    
    public Reputation() {
        this.score = 0;
    }
    
    // accessors
    public int getReputationTier() {
        for (int i = 0; i < TIERS.length; i++) {
            if (score < TIERS[i]) {
                return i;
            }
        }
        return TIERS.length;
    }

    // mutators
    public void adjustScore(int adjustment) {
        if ((score + adjustment >= MIN_SCORE) && (score + adjustment <= MAX_SCORE)) {
            score += adjustment;
        }
    }
}