package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.data.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

public class UserRepository {

    /**
     * Updates only the wallet balance for a specific user.
     */
    public static void updateWallet(int userId, float newBalance, Callback<Void> callback) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("wallet", newBalance);

        String idQuery = "eq." + userId;

        SupabaseClient.getApi()
                .updateWallet(idQuery, updateData)
                .enqueue(callback);
    }

    /**
     * Fetches the latest user data from Supabase by username.
     */
    public static void fetchUserByUsername(String username, Callback<List<User>> callback) {
        String query = "eq." + username;
        SupabaseClient.getApi().getUserByUsername(query).enqueue(callback);
    }
}
