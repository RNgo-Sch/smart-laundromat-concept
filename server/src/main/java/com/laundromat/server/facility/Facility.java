package com.laundromat.server.facility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.laundromat.server.model.Machine;
import com.laundromat.server.model.User;
import com.laundromat.server.queue.MachineQueue;

import jakarta.annotation.PreDestroy;

public class Facility {
    private MachineQueue washerQueue;
    private MachineQueue dryerQueue;

    private ScheduledExecutorService queueChecker;
    
    public Facility(Machine[] washers, Machine[] dryers) {
        this.washerQueue = new MachineQueue(washers);
        this.dryerQueue = new MachineQueue(dryers);
    }

    // app request related methods
    public void queueForWasher(User queuer) {
        this.washerQueue.joinQueue(queuer);
    }
    public void queueForDryer(User queuer) {
        this.dryerQueue.joinQueue(queuer);
    }

    // queue monitoring methods
    public void startMonitoringQueues() {
        System.out.println("checking queue");
        this.queueChecker = Executors.newSingleThreadScheduledExecutor();
        this.queueChecker.scheduleAtFixedRate(() -> {
            if (!washerQueue.isEmpty() && washerQueue.hasAvailableMachine()) {
                washerQueue.updateQueue();
            }
            if (!dryerQueue.isEmpty() && dryerQueue.hasAvailableMachine()) {
                dryerQueue.updateQueue();
            }
            // TODO Sync machines to Supabase
        }, 0, 5, TimeUnit.SECONDS);
    }
    
    // misc
    @Override
    public String toString() {
        String out = "Facility overview:";
        out += "\tWashers:\n" + washerQueue.toString() + "\n\tDryers:\n" + dryerQueue.toString();
        return out;
    }
    @PreDestroy // when spring web is terminated
    public void stopMonitoringQueues() {
        if (this.queueChecker != null) {
            this.queueChecker.shutdownNow();
            this.queueChecker = null;
            System.out.println("queue checking stopped");
        }
    }
}
