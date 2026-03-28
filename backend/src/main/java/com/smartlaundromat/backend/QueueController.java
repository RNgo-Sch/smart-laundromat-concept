package com.smartlaundromat.backend;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/queue")
@CrossOrigin(origins = "*")
public class QueueController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    // Two separate queues
    private final Queue<String> washerQueue = new LinkedList<>();
    private final Queue<String> dryerQueue  = new LinkedList<>();

    // Two separate assignment maps
    private final Map<String, MachineAssignment> washerAssignments = new HashMap<>();
    private final Map<String, MachineAssignment> dryerAssignments  = new HashMap<>();
    @GetMapping("/test-db")
    public String testDB() {
        return jdbcTemplate.queryForObject("SELECT 'DB Connected!'", String.class);
    }



    // -------------------------------------------------------------------------
    // Endpoints
    // -------------------------------------------------------------------------

    @GetMapping("/join")
    public QueueResponse joinQueue(
            @RequestParam String user,
            @RequestParam String type
    ) {
        QueueResponse response = new QueueResponse();

        if (type.equals("washer")) {
            return joinSpecificQueue(user, washerQueue, washerAssignments, "washer");
        } else {
            return joinSpecificQueue(user, dryerQueue, dryerAssignments, "dryer");
        }
    }

    @GetMapping("/position")
    public QueueResponse getPosition(
            @RequestParam String user,
            @RequestParam String type
    ) {
        QueueResponse response = new QueueResponse();

        Queue<String> queue = type.equals("washer") ? washerQueue : dryerQueue;
        Map<String, MachineAssignment> assignments = type.equals("washer") ? washerAssignments : dryerAssignments;

        int position = getPositionOf(user, queue);
        if (position == -1) {
            response.message = "User not in " + type + " queue";
            return response;
        }

        MachineAssignment assignment = assignments.get(user);
        response.message       = "Position in " + type + " queue: " + position;
        response.machineType   = assignment.machineType;
        response.machineNumber = assignment.machineNumber;
        return response;
    }

    @GetMapping("/cancel")
    public QueueResponse cancel(
            @RequestParam String user,
            @RequestParam String type
    ) {
        QueueResponse response = new QueueResponse();

        Queue<String> queue = type.equals("washer") ? washerQueue : dryerQueue;
        Map<String, MachineAssignment> assignments = type.equals("washer") ? washerAssignments : dryerAssignments;

        if (queue.remove(user)) {
            assignments.remove(user);
            response.message = user + " removed from " + type + " queue";
        } else {
            response.message = "User not found in " + type + " queue";
        }
        return response;
    }

    @GetMapping("/next")
    public QueueResponse nextUser(@RequestParam String type) {
        QueueResponse response = new QueueResponse();

        Queue<String> queue = type.equals("washer") ? washerQueue : dryerQueue;
        Map<String, MachineAssignment> assignments = type.equals("washer") ? washerAssignments : dryerAssignments;

        if (queue.isEmpty()) {
            response.message = type + " queue is empty";
            return response;
        }

        String user = queue.poll();
        MachineAssignment assignment = assignments.remove(user);
        response.message       = "Next user in " + type + " queue: " + user;
        response.machineType   = assignment != null ? assignment.machineType : "";
        response.machineNumber = assignment != null ? assignment.machineNumber : 0;
        return response;
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    private QueueResponse joinSpecificQueue(
            String user,
            Queue<String> queue,
            Map<String, MachineAssignment> assignments,
            String type
    ) {
        QueueResponse response = new QueueResponse();

        // Prevent duplicate entries
        if (queue.contains(user)) {
            MachineAssignment existing = assignments.get(user);
            response.message       = user + " is already in " + type + " queue. Position: " + getPositionOf(user, queue);
            response.machineType   = existing.machineType;
            response.machineNumber = existing.machineNumber;
            return response;
        }

        // Assign machine of the correct type
        MachineAssignment assignment = new MachineAssignment();
        assignment.machineType = type;

        assignment.machineNumber = findBestMachine(type);

        queue.add(user);
        assignments.put(user, assignment);

        response.message       = user + " joined " + type + " queue. Position: " + queue.size();
        response.machineType   = assignment.machineType;
        response.machineNumber = assignment.machineNumber;
        return response;
    }

    private int getPositionOf(String user, Queue<String> queue) {
        int position = 1;
        for (String u : queue) {
            if (u.equals(user)) return position;
            position++;
        }
        return -1;
    }
    private int findBestMachine(String type) {

        // Simulate: pick machine with smallest queue load
        // (you can later replace with real machine states)

        int minMachine = 1;
        int minLoad = Integer.MAX_VALUE;

        Map<String, MachineAssignment> assignments;
        if (type.equals("washer")) {
            assignments = washerAssignments;
        } else if (type.equals("dryer")) {
            assignments = dryerAssignments;
        } else {
            throw new IllegalArgumentException("Unknown machine type: " + type);
        }

        int[] load = new int[5]; // index 1–4

        for (MachineAssignment a : assignments.values()) {
            load[a.machineNumber]++;
        }

        for (int i = 1; i <= 4; i++) {
            if (load[i] < minLoad) {
                minLoad = load[i];
                minMachine = i;
            }
        }

        return minMachine;
    }
    @GetMapping("/machines")
    public List<Map<String, Object>> getMachines() {
        return jdbcTemplate.queryForList("SELECT * FROM machine");
    }

    // -------------------------------------------------------------------------
    // Inner Classes
    // -------------------------------------------------------------------------

    static class MachineAssignment {
        String machineType;
        int machineNumber;
    }

    static class QueueResponse {
        public String message;
        public String machineType;
        public int machineNumber;
    }
}