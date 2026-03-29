package com.laundromat.server.queue;

import java.util.PriorityQueue;

import com.laundromat.server.model.Machine;

public class MachineQueue extends PriorityQueue<QueueMember> {
    private Machine[] machines;

    public MachineQueue(Machine[] machines) {
        this.machines = machines;
    }

    // queue related methods
    public void joinQueue(int queuer) {
        // assign machine if possible
        if (isEmpty()) {
            Machine m = this.findAvailableMachine();
            if (m != null) {
                m.useMachine(queuer);
            } else {
                this.add(new QueueMember(queuer));
            }
            // cannot assign or queue already has people waiting
        } else if (!contains(queuer)) {
            add(new QueueMember(queuer));
        }
    }
    public void leaveQueue(int queuer) {
        if (contains(queuer)) {
            remove(queuer);
        }
    }
    public void updateQueue() {
        // method called because available machine for user
        int u = this.poll().getQueuer().getId();
        Machine m = findAvailableMachine();
        m.useMachine(u);
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

    // facility helper methods
    public void interactWith(int userId, int machineId) {
        Machine m = getMachine(machineId);
        m.useMachine(userId);
    }

    // machine array helper methods
    public boolean containsMachine(int machineId) {
        return getMachine(machineId) != null;
    }
    private Machine getMachine(int machineId) {
        for (Machine m: this.machines) {
            if (m.getId() == machineId) {
                return m;
            }
        }
        return null;
    }

    private boolean contains(int queuer) {
        for (Object o: this.toArray()) {
            if (o instanceof QueueMember m && m.getQueuer().getId() == queuer) {
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
