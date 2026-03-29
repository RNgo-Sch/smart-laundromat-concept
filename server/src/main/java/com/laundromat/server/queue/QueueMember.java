package com.laundromat.server.queue;

import java.util.Objects;

import com.laundromat.server.model.User;
import com.laundromat.server.db.Query;

// TODO rewrite to only have userId instead of whole object

public class QueueMember implements Comparable<QueueMember> {
    private final static int REPUTATION_BONUS = 1;

    private final User queuer;
    private int priority;

    public QueueMember(int queuer) {
        this.queuer = Query.userFromId(queuer);
        this.priority = (this.queuer.reputation.getReputationTier() * REPUTATION_BONUS);
    }

    public User getQueuer() {
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
        if (obj instanceof QueueMember other) return this.queuer.equals(other.queuer);
        if (obj instanceof User other) return this.queuer.equals(other);
        return false;
    }
    @Override
    public int hashCode() {
        // Must match the field used in equals()
        return Objects.hash(this.queuer);
    }
}
