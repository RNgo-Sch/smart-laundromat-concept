package com.laundromat.server.queue;

import java.util.Objects;

import com.laundromat.server.db.Query;
import com.laundromat.server.model.User;

public class QueueMember implements Comparable<QueueMember> {
    private final static int REPUTATION_BONUS = 1;

    private final int queuer;
    private int priority;

    public QueueMember(int queuer) {
        this.queuer = queuer; 
        Query.userFromId(queuer);
        this.priority = (Query.userFromId(queuer).reputation.getReputationTier() * REPUTATION_BONUS);
    }

    public int getQueuer() {
        return queuer;
    }
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(QueueMember other) {
        return other.getPriority() - this.getPriority();
    }
    @Override
    public String toString() {
        return "Queuing " + queuer + "\n\tPriority: " + priority;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof QueueMember other) return this.queuer == other.queuer;
        if (obj instanceof User other) return this.queuer == other.getId();
        return false;
    }
    @Override
    public int hashCode() {
        // Must match the field used in equals()
        return Objects.hash(this.queuer);
    }
}
