package com.laundromat.server.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.laundromat.server.idcounter.IdCounter;
import com.laundromat.server.db.Query;

public class Machine {
    public static enum State {
        AVAILABLE {
            // this state does not have currentUser or nextTime
            @Override
            public State timeOut(Machine m) {
                return AVAILABLE; // should be no case where timeOut() in AVAILABLE to be called
            }
            @Override
            public State interact(Machine m, int u) {
                // User which interacted reserved machine
                m.setCurrentUser(u);
                m.setNextTime(10);
                return RESERVED;
            }
        },
        RESERVED {
            // this state has currentUser and 10s nextTime
            @Override
            public State timeOut(Machine m) {
                // User did not use machine within their reserved time
                m.clearNextTime();
                m.punishCurrentUser();
                m.clearCurrentUser();
                return AVAILABLE;
            }
            @Override
            public State interact(Machine m, int u) {
                // if same user, they start the washing cycle
                if (m.getCurrentUser().getId() == u) {
                    m.setNextTime(30);
                    return INUSE;
                } else {
                    // no effect if different user
                    return RESERVED;
                }
            }
        },
        INUSE {
            // this state has current user and 30s nextTime
            @Override
            public State timeOut(Machine m) {
                // laundry cycle complete
                m.setNextTime(10);
                return COLLECTION;
            }
            @Override
            public State interact(Machine m, int u) {
                return INUSE; // ignore interaction during state
            }
        },
        COLLECTION {
            @Override
            public State timeOut(Machine m) {
                // user fails to collect laundry on time
                m.clearNextTime();
                m.punishCurrentUser();
                m.clearCurrentUser();
                return AVAILABLE;
            }
            @Override
            public State interact(Machine m, int u) {
                // user collects laundry on time
                if (m.getCurrentUser().getId() == u) {
                    m.clearNextTime();
                    m.clearCurrentUser();
                    return AVAILABLE;
                } else {
                    return COLLECTION; // ignore interaction from other users
                }
            }
        };

        public abstract State timeOut(Machine m);
        public abstract State interact(Machine m, int u);
    }

    private final Integer id;
    private State state;
    private User currentUser;
    private ScheduledExecutorService nextTime;

    // TODO rewrite constructor to be supabase friendly
    public Machine(int machineId) {
        this.id = machineId;
        this.state = State.AVAILABLE;
        this.currentUser = null;
        this.nextTime = Executors.newSingleThreadScheduledExecutor();
    }

    // methods
    public void timeOut() {
        this.state = this.state.timeOut(this);
        System.out.print("Timeout triggered; " + this.toString() + "\n");
    }
    public void useMachine(int userId) {
        this.state = this.state.interact(this, userId);
        System.out.print("User " + userId + " interacted; " + this.toString() + "\n");
    }
    protected void punishCurrentUser() {
        if (this.currentUser != null) {
            // punishment logic
            System.out.println("User punishment applied");
        }
    }
    // getters
    public int getId() {
        return this.id;
    }
    public User getCurrentUser() {
        return this.currentUser;
    }
    public String getState() {
        return this.state.toString();
    }

    // setters
    protected void setCurrentUser(int userId) {
        this.currentUser = Query.userFromId(userId);
    }
    protected void clearCurrentUser() {
        this.currentUser = null;
    }
    protected void setNextTime(long seconds) {
        this.clearNextTime();
        this.nextTime = Executors.newSingleThreadScheduledExecutor();
        this.nextTime.schedule(this::timeOut, seconds, TimeUnit.SECONDS);
    }
    protected void clearNextTime() {
        if (this.nextTime != null) {
            this.nextTime.shutdownNow();
            this.nextTime = null;
        }
    }

    // misc
    @Override
    public String toString() {
        return "Machine " + this.id + " state: " + this.getState() + "; currentUser: " + this.getCurrentUser() + "; Executor: " + this.nextTime;
    }
}
