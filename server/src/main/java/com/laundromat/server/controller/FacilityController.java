package com.laundromat.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laundromat.server.facility.Facility;

@RestController
@RequestMapping("")
public class FacilityController {

    private final Facility facility;

    public FacilityController(Facility facility) {
        this.facility = facility;
    }

    // POST /queue/washer?userId=<_>
    @PostMapping("/queue/washer")
    public ResponseEntity<?> joinWasherQueue(@RequestParam int userId) {
        facility.queueForWasher(userId);
        return ResponseEntity.ok("Joined washer queue");
    }

    // POST /queue/dryer?userId=<_>
    @PostMapping("/queue/dryer")
    public ResponseEntity<?> joinDryerQueue(@RequestParam int userId) {
        facility.queueForDryer(userId);
        return ResponseEntity.ok("Joined dryer queue");
    }

    // POST /interact?userId=<_>&machineId=<_>
    @PostMapping("/interact")
    public ResponseEntity<?> interactWithMachine(@RequestParam int userId, @RequestParam int machineId) {
        // TODO implement machine interaction
        return ResponseEntity.ok("Interacted with machine");
    }
}
