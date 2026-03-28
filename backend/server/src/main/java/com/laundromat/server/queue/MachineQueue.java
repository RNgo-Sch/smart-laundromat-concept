package com.laundromat.server.queue;

import java.util.PriorityQueue;

import com.laundromat.server.model.Machine;
import com.laundromat.server.model.User;

public class MachineQueue extends PriorityQueue<QueueMember> {
    private Machine[] machines;

    public MachineQueue(Machine[] machines) {
        this.machines = machines;
    }

    // queue related methods
    public void joinQueue(User queuer) {
        // assign machine if possible
        if (isEmpty()) {
            Machine m = this.findAvailableMachine();
            if (m != null) {
                this.assignToMachine(queuer, m);
            } else {
                this.add(new QueueMember(queuer));
            }
        // cannot assign or queue already has people waiting
        } else if (!contains(queuer)) {
            add(new QueueMember(queuer));
        }
    }
    public void leaveQueue(User queuer) {
        QueueMember qm = new QueueMember(queuer);
        if (contains(qm)) {
            remove(qm);
        }
    }
    public void updateQueue() {
        // method called because available machine for user
        User u = this.poll().getQueuer();
        Machine m = findAvailableMachine();
        assignToMachine(u, m);
    }

    // queue helper methods
    private Machine findAvailableMachine() {
        for (Machine m: machines) {
            if (m.getState().equals("AVAILABLE")) {
                return m;
            }
        }
        return null;
    }
    public boolean hasAvailableMachine() {
        return findAvailableMachine() != null;
    }
    public User getUserFromQueue() {
        if (!isEmpty()) {
            return poll().getQueuer();
        } else {
            return null;
        }
    }
    private void assignToMachine(User q, Machine m) {
        // previous logic guarantees m is in AVAILABLE state
        m.useMachine(q);
    }

    private boolean contains(User u) {
        for (Object o: this.toArray()) {
            if (o.equals(u)) {
                return true;
            }
        }
        return false;
    }

    //misc
    @Override
    public String toString() {
        String out = "";
        for (Machine m: this.machines) {
            out += "\t" + m.toString() + "\n";
        } 
        return out;
    }
}
