package com.example.smart_laundromat_concept.data.remote.repository;

import com.example.smart_laundromat_concept.data.model.AppMachine;
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

        // Washers
        SupabaseClient.getApi()
                .getMachinesByType("eq." + STORE_NUMBER, "eq.washer")
                .enqueue(new Callback<List<AppMachine>>() {
                    @Override
                    public void onResponse(Call<List<AppMachine>> call, Response<List<AppMachine>> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            for (AppMachine m : response.body()) {
                                AppMachine.State state = AppMachine.State.fromString(m.status);
                                AppMachine.setWasherState(m.position, state);
                            }
                        }

                        // After washers → fetch dryers
                        fetchDryers(onComplete);
                    }

                    @Override
                    public void onFailure(Call<List<AppMachine>> call, Throwable t) {
                        fetchDryers(onComplete); // still continue
                    }
                });
    }

    private static void fetchDryers(Runnable onComplete) {

        SupabaseClient.getApi()
                .getMachinesByType("eq." + STORE_NUMBER, "eq.dryer")
                .enqueue(new Callback<List<AppMachine>>() {
                    @Override
                    public void onResponse(Call<List<AppMachine>> call, Response<List<AppMachine>> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            for (AppMachine m : response.body()) {
                                AppMachine.State state = AppMachine.State.fromString(m.status);
                                AppMachine.setDryerState(m.position, state);
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
}