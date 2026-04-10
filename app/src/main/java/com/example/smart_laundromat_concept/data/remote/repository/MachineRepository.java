package com.example.smart_laundromat_concept.data.remote.repository;

import com.example.smart_laundromat_concept.data.model.AppMachine;
import com.example.smart_laundromat_concept.data.remote.supabase.SupabaseClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for fetching machine states from Supabase.
 * Used by BookingActivity to get a live view of washer/dryer status.
 */
public class MachineRepository {

    private static final int STORE_NUMBER = 1;

    public static void fetchAllMachines(Runnable onComplete) {
        fetchWashers(onComplete);
    }

    private static void fetchWashers(Runnable onComplete) {

        SupabaseClient.getApi()
                .getMachinesByType("eq." + STORE_NUMBER, "eq.washer", "*")
                .enqueue(new Callback<List<AppMachine>>() {
                    @Override
                    public void onResponse(Call<List<AppMachine>> call, Response<List<AppMachine>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (AppMachine m : response.body()) {
                                AppMachine.setWasherState(
                                        m.position,
                                        AppMachine.State.fromString(m.status),
                                        m.currentUser
                                );
                            }
                        }
                        fetchDryers(onComplete);
                    }

                    @Override
                    public void onFailure(Call<List<AppMachine>> call, Throwable t) {
                        fetchDryers(onComplete);
                    }
                });
    }

    private static void fetchDryers(Runnable onComplete) {

        SupabaseClient.getApi()
                .getMachinesByType("eq." + STORE_NUMBER, "eq.dryer", "*")
                .enqueue(new Callback<List<AppMachine>>() {
                    @Override
                    public void onResponse(Call<List<AppMachine>> call, Response<List<AppMachine>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (AppMachine m : response.body()) {
                                AppMachine.setDryerState(
                                        m.position,
                                        AppMachine.State.fromString(m.status),
                                        m.currentUser
                                );
                            }
                        }
                        if (onComplete != null) onComplete.run();
                    }

                    @Override
                    public void onFailure(Call<List<AppMachine>> call, Throwable t) {
                        if (onComplete != null) onComplete.run();
                    }
                });
    }

    /**
     * Updates a single machine locally from realtime updates.
     * This avoids refetching all machines.
     */
    public static void updateMachineLocally(String type, int position, String status, Integer currentUser) {
        AppMachine.State state = AppMachine.State.fromString(status);

        if ("washer".equalsIgnoreCase(type)) {
            AppMachine.setWasherState(position, state, currentUser);
        } else {
            AppMachine.setDryerState(position, state, currentUser);
        }
    }

    public static String getMachineStatus(int id) {

        if (AppMachine.getWashers().containsKey(id)) {
            return AppMachine.getWashers().get(id).getState().toString();
        }

        if (AppMachine.getDryers().containsKey(id)) {
            return AppMachine.getDryers().get(id).getState().toString();
        }

        return null;
    }


}