package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.data.model.AppMachine;
import com.example.smart_laundromat_concept.data.model.Machine;

import java.util.List;

import retrofit2.Callback;

/**
 * Repository for fetching machine states from Supabase.
 * Used by BookingActivity to get a live view of washer/dryer status.
 */
public class MachineRepository {

    private static final int STORE_NUMBER = 1;

    /**
     * Fetches all washers from Supabase for the current store.
     *
     * @param callback Retrofit callback
     */
    public static void getWashers(Callback<List<Machine>> callback) {
        SupabaseClient.getApi()
                .getMachinesByStoreAndType(STORE_NUMBER, "washer")
                .enqueue(callback);
    }

    /**
     * Fetches all dryers from Supabase for the current store.
     *
     * @param callback Retrofit callback
     */
    public static void getDryers(Callback<List<Machine>> callback) {
        SupabaseClient.getApi()
                .getMachinesByStoreAndType(STORE_NUMBER, "dryer")
                .enqueue(callback);
    }
}