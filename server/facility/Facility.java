package server.facility;

import java.util.PriorityQueue;
import java.util.Optional;

import server.idcounter.IdCounter;
import server.user.User;
import server.queue.QueueMember;

public class Facility {
    private static IdCounter latestId = new IdCounter(); // most recent id value used

    private final int id;
    private PriorityQueue<QueueMember> queue;
    private int[] machines; // placeholder
    
    public Facility() {
        id = latestId.getId();
        machines = new int[]{1, 2, 3};
        queue = new PriorityQueue<QueueMember>();
    }

    public void queueForFacility(User queuer) {
        Optional<Integer> availableMachine = checkForMachine();
        if (availableMachine.isEmpty()) {
            queue.add(new QueueMember(queuer));
        } else {
            AssignMachine(queuer, availableMachine.get());
        }
    }

    private Optional<Integer> checkForMachine() {
        for (int m: machines) {
            if (m == 2) {   // replace with availability status
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    private void AssignMachine(User user, int Machine) {
        // implement later
    }

    // misc
    @Override
    public String toString() {
        return "Facility " + id + "\n\tQueue: " + queue + "\n\tMachines: " + machines;
    }
    public static String getInfo() {
        return "Facilities registers: " + latestId;
    }
}
