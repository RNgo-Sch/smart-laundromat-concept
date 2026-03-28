package com.laundromat.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Server is running on port 8080!";
    }

    @GetMapping("/test-db")
    public String testDb() {
        return "DB connected!";
    }
}