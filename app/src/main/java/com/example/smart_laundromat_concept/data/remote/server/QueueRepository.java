package com.example.smart_laundromat_concept.data.remote.server;

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

    public static void joinQueue(String userId, String type, QueueCallback callback) {

        BackendClient.BackendApi api = BackendClient.getApi();

        Call<Void> call;

        if ("washer".equalsIgnoreCase(type)) {
            call = api.joinWasher(userId);
        } else {
            call = api.joinDryer(userId);
        }

        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void cancelQueue(String userId, String machineType, QueueCallback callback) {
        BackendClient.BackendApi api = BackendClient.getApi();

        Call<Void> call;

        if ("washer".equalsIgnoreCase(machineType)) {
            call = api.leaveWasher(userId);
        } else {
            call = api.leaveDryer(userId);
        }

        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void interact(int userId, int machineId, QueueCallback callback) {
        BackendClient.getApi()
                .interact(userId, machineId)
                .enqueue(new retrofit2.Callback<Void>() {

                    @Override
                    public void onResponse(retrofit2.Call<Void> call,
                                           retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(new QueueResponse());
                        } else {
                            callback.onError("Failed to start machine");
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public static void getPosition(String machineType, Callback<QueueResponse> callback) {
        String username = UserSession.getInstance().getUsername();
        BackendClient.getApi().getPosition(username, machineType).enqueue(callback);
    }

    public static void getMachines(Callback<Map<String, Map<Integer, String>>> callback) {
        BackendClient.getApi().getMachines().enqueue(callback);
    }
}