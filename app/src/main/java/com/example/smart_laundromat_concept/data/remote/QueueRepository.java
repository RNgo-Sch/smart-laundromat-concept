package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.data.model.QueueResponse;
import com.example.smart_laundromat_concept.data.session.UserSession;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class for all queue-related network operations.
 * Communicates with the Spring Boot backend.
 */
public class QueueRepository {

    public static void joinQueue(String userId, String machineType, QueueCallback callback) {

        Call<QueueResponse> call;

        if ("washer".equalsIgnoreCase(machineType)) {
            call = BackendClient.getApi().joinWasher(userId);
        } else {
            call = BackendClient.getApi().joinDryer(userId);
        }

        call.enqueue(new Callback<QueueResponse>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to join queue");
                }
            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {
                callback.onError("Network error");
            }
        });
    }


    public static void getPosition(String machineType, Callback<QueueResponse> callback) {
        String username = UserSession.getInstance().getUsername();
        BackendClient.getApi().getPosition(username, machineType).enqueue(callback);
    }

    public static void cancelQueue(String userId, String machineType, QueueCallback callback) {

        Call<QueueResponse> call;

        if ("washer".equalsIgnoreCase(machineType)) {
            call = BackendClient.getApi().cancelWasher(userId);
        } else {
            call = BackendClient.getApi().cancelDryer(userId);
        }

        call.enqueue(new Callback<QueueResponse>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to leave queue");
                }
            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {
                callback.onError("Network error");
            }
        });
    }

    public static void getMachines(Callback<Map<String, Map<Integer, String>>> callback) {
        BackendClient.getApi().getMachines().enqueue(callback);
    }
}