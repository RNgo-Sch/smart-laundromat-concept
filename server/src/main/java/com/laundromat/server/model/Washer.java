package com.laundromat.server.model;

public class Washer extends Machine {

    public Washer(int id, State state, User currentUser) {
        super(id, state, currentUser);
    }

    @Override
    protected int getDuration() {
        return 20;
    }
    @Override
    protected int getGracePeriod() {
        return 10;
    }
    @Override
    protected float getPrice() {
        return 2.0f;
    }
}