package com.example.smart_laundromat_concept.data.model;

public class AppMachine {
    public enum State {
        AVAILABLE {
            @Override
            public String toString() {
                return "Available";
            }
        },
        RESERVED {
            @Override
            public String toString() {
                return "Reserved";
            }
        },
        INUSE {
            @Override
            public String toString() {
                return "In Use";
            }
        },
        COLLECTION {
            @Override
            public String toString() {
                return "Waiting";
            }
        },
        OOS {
            @Override
            public String toString() {
                return "Out of Service";
            }
        }
    }

    private final int id;
    private State state;
    private final String type;

    public AppMachine(int id, State state, String type) {
        this.id = id;
        this.state = state;
        this.type = type;
    }

    // getters
    public int getId() {
        return this.id;
    }
    public String getState() {
        return this.state.toString();
    }
    public String getMachineType() {
        return this.type;
    }

    // setters
    public void changeState(State newState) {
        this.state = newState;
    }
}
