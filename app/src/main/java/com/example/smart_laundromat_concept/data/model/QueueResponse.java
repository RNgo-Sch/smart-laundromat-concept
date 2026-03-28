package com.example.smart_laundromat_concept.data.model;

/**
 * Represents the response from the Spring Boot queue endpoint.
 * Contains the message and the machine assigned to the user.
 */
public class QueueResponse {
    public String message;
    public String machineType;  // "washer" or "dryer"
    public int machineNumber;   // 1 to 4
}