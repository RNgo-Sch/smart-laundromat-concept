package com.smartlaundromat.backend;

import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Queue;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private Queue<String> queue = new LinkedList<>();

    // Join queue
    @GetMapping("/join")
    public String joinQueue(@RequestParam String user) {
        queue.add(user);
        return user + " joined queue. Position: " + queue.size();
    }

    // Get position
    @GetMapping("/position")
    public String getPosition(@RequestParam String user) {
        int position = 1;
        for (String u : queue) {
            if (u.equals(user)) {
                return "Position: " + position;
            }
            position++;
        }
        return "User not in queue";
    }

    // Call next user
    @GetMapping("/next")
    public String nextUser() {
        if (queue.isEmpty()) {
            return "Queue is empty";
        }
        return "Next user: " + queue.poll();
    }

    // Cancel queue
    @GetMapping("/cancel")
    public String cancel(@RequestParam String user) {
        if (queue.remove(user)) {
            return user + " removed from queue";
        }
        return "User not found";
    }
}