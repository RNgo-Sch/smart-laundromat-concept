package com.laundromat.server.model;

public class Dryer extends Machine {

    public Dryer(int id, State state, User currentUser) {
        super(id, state, currentUser);
    }

    @Override
    protected int getDuration() {
        return 30;
    }
    @Override
    protected int getGracePeriod() {
        return 10;
    }
    @Override
    protected float getPrice() {
        return 3.0f;
    }
}