package com.laundromat.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laundromat.server.facility.Facility;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class FacilityController {

    private final Facility facility;
    private final JdbcTemplate jdbcTemplate;

    // Spring injects both Facility and JdbcTemplate automatically
    public FacilityController(Facility facility, JdbcTemplate jdbcTemplate) {
        this.facility       = facility;
        this.jdbcTemplate   = jdbcTemplate;
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

    // GET /machines — returns all machines from Supabase
    @GetMapping("/machines")
    public List<Map<String, Object>> getMachines() {
        return jdbcTemplate.queryForList(
                "SELECT id, type, status, \"current_user\" FROM machine"
        );
    }
}