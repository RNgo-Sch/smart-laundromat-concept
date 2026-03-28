package com.laundromat.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laundromat.server.facility.Facility;
import com.laundromat.server.model.User;

@RestController
@RequestMapping("/queue")
public class FacilityController {

    private final Facility facility;

    public FacilityController(Facility facility) {
        this.facility = facility;
    }



    // POST /queue/washer/join
    @PostMapping("/washer/join")
    public ResponseEntity<?> joinWasherQueue(@RequestBody UserRequest body) {
        facility.queueForWasher(new User());
        return ResponseEntity.ok("Joined washer queue");
    }

    // POST /queue/dryer/join
    @PostMapping("/dryer/join")
    public ResponseEntity<?> joinDryerQueue(@RequestBody UserRequest body) {
        facility.queueForDryer(new User());
        return ResponseEntity.ok("Joined dryer queue");
    }

    record UserRequest(int userId) {}
}
