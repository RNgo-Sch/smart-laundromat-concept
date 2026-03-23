package com.example.smart_laundromat_concept.data.model;



public class Reputation {
    private static final int MIN_SCORE = -10;
    private static final int MAX_SCORE = 120;
    private static final int[] TIERS = {10, 25, 50, 100};

    private int score;

    public Reputation(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getTier() {
        for (int i = 0; i < TIERS.length; i++) {
            if (score < TIERS[i]) return i;
        }
        return TIERS.length;
    }

    public void adjustScore(int adjustment) {
        int newScore = score + adjustment;
        if (newScore >= MIN_SCORE && newScore <= MAX_SCORE) {
            score = newScore;
        }
    }
}