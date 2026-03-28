package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.data.model.QueueResponse;
import com.example.smart_laundromat_concept.data.session.UserSession;

import retrofit2.Callback;

/**
 * Repository class for all queue-related network operations.
 * Communicates with the Spring Boot backend.
 */
public class QueueRepository {

    public static void joinQueue(String user, String type, Callback<QueueResponse> callback) {
        BackendClient.getApi().joinQueue(user, type).enqueue(callback);
    }


    public static void getPosition(String machineType, Callback<QueueResponse> callback) {
        String username = UserSession.getInstance().getUsername();
        BackendClient.getApi().getPosition(username, machineType).enqueue(callback);
    }

    public static void cancelQueue(String user, String type, Callback<QueueResponse> callback) {
        BackendClient.getApi().cancelQueue(user, type).enqueue(callback);
    }
}