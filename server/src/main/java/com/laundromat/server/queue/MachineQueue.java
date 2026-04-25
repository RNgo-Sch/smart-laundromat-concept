package com.laundromat.server.queue;

import java.util.PriorityQueue;

import com.laundromat.server.model.Machine;
import com.laundromat.server.model.User;

public class MachineQueue {
    private final Machine[] machines;
    private final PriorityQueue<User> userQueue;

    public MachineQueue(Machine[] machines) {
        this.machines = machines;
        this.userQueue = new PriorityQueue<>();
    }

    // queue related methods
    public void joinQueue(int queuer) {
        // assign machine if possible
        if (userQueue.isEmpty()) {
            Machine m = findAvailableMachine();
            if (hasAvailableMachine()) {
                m.useMachine(queuer);
            } else {
                userQueue.add(User.fromId(queuer));
            }
        // cannot assign or queue already has people waiting
        } else if (!userQueue.contains(queuer)) {
            userQueue.add(User.fromId(queuer));
        }
    }
    public void leaveQueue(int queuer) {
        User u = User.fromId(queuer);
        if (userQueue.contains(u)) {
            System.out.println("MachineQueue: queuer " +queuer+ " found, removing");
            userQueue.remove(u);
        } else {
            System.out.println("MachineQueue: queuer " +queuer+ "not found");
        }
    }
    public void updateQueue() {
        // method called because available machine for user
        int u = userQueue.poll().getId();
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
    public boolean isEmpty() {
        return userQueue.isEmpty();
    }

    // facility helper methods
    public void interactWith(int userId, int machineId) {
        Machine m = getMachine(machineId);
        m.useMachine(userId);
    }

    // machine array helper methods
    public Machine[] getMachines() {
        return machines;
    }
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

    //misc
    @Override
    public String toString() {
        String out = "";
        for (Machine m: machines) {
            out += "\t" + m.toString() + "\n";
        }
        return out;
    }
}
