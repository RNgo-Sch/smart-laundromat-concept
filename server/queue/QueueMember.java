package server.queue;

import server.user.User;

public class QueueMember implements Comparable<QueueMember>{
    private final static int DEFAULT_PRIORITY = 50;
    private final static int REPUTATION_BONUS = 5;
    
    private User queuer;
    private int priority;

    public QueueMember(User queuer) {
        this.queuer = queuer;
        this.priority = DEFAULT_PRIORITY - (queuer.getReputation().getReputationTier() * REPUTATION_BONUS);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int newPriority) {
        this.priority = newPriority;
    }

    @Override
    public int compareTo(QueueMember other) {
        if (this.getPriority() < other.getPriority()) {
            return -1;
        } else if (this.getPriority() == other.getPriority()) {
            return 0;
        } else {
            return 1;
        }
    }
    public String toString() {
        return "Queuing " + queuer + "\n\tPriority: " + priority;
    }
}
