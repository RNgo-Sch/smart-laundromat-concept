package com.laundromat.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laundromat.server.db.Query;
import com.laundromat.server.facility.Facility;

//mvn spring-boot:run
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
        System.out.println("FacilityController: washer queue request from user "+userId);
        facility.queueForWasher(userId);
        return ResponseEntity.ok("Joined washer queue");
    }

    // POST /queue/dryer?userId=<_>
    @PostMapping("/queue/dryer")
    public ResponseEntity<?> joinDryerQueue(@RequestParam int userId) {
        System.out.println("FacilityController: dryer queue request from user "+userId);
        facility.queueForDryer(userId);
        return ResponseEntity.ok("Joined dryer queue");
    }

    // POST /leave/washer?userId=<_>
    @PostMapping("/leave/washer")
    public ResponseEntity<?> leaveWasherQueue(@RequestParam int userId) {
        System.out.println("FacilityController: washer leave queue request from user "+userId);
        facility.leaveForWasher(userId);
        return ResponseEntity.ok("Left dryer queue");
    }

    // POST /leave/dryer?userId=<_>
    @PostMapping("/leave/dryer")
    public ResponseEntity<?> leaveDryerQueue(@RequestParam int userId) {
        System.out.println("FacilityController: washer leave queue request from user "+userId);
        facility.leaveForDryer(userId);
        return ResponseEntity.ok("Left dryer queue");
    }

    // POST /interact?userId=<_>
    @PostMapping("/interact")
    public ResponseEntity<?> interactWithMachine(@RequestParam int userId) {
        int machineId;
        try {
            machineId = Query.machineIdFromUserId(userId);
            System.out.println("FacilityController: interact request from "+userId+" for machine "+machineId);
            facility.interactWith(userId, machineId);
            return ResponseEntity.ok("Interacted with machine");
        } catch (Exception e) {
            return ResponseEntity.ok("No machine found for user");
        }
        
    }
}