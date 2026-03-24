
package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.data.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

/**
 * Repository class responsible for handling all user-related network operations.
 * <p>
 * This class communicates with Supabase via Retrofit API calls.
 * It does NOT store data locally — it only sends and receives data from the backend.
 */
public class UserRepository {

    /**
     * Updates the user's wallet balance in the Supabase database.
     * <p>
     * This method sends a PATCH request to update only the "wallet" field
     * for the specified user ID.
     *
     * @param userId      the unique ID of the user to update
     * @param newBalance  the new wallet balance to set
     * @param callback    Retrofit callback to handle success or failure
     */
    public static void updateBalance(int userId, float newBalance, Callback<Void> callback) {
        // Create a map to hold fields to update (partial update / PATCH request)
        Map<String, Object> updateData = new HashMap<>();
        // Specify that we are updating only the "wallet" column
        updateData.put("wallet", newBalance);

        // Supabase filter: selects user where id = userId
        String idQuery = "eq." + userId;

        // Send the update request asynchronously to Supabase
        SupabaseClient.getApi()
                .updateWallet(idQuery, updateData)
                .enqueue(callback);
    }

    /**
     * Fetches user data from Supabase using the username.
     * <p>
     * This method sends a GET request and returns a list of matching users.
     * Typically, only one user should match the username.
     *
     * @param username the username to search for
     * @param callback Retrofit callback to handle the response
     */
    public static void fetchUserByUsername(String username, Callback<List<User>> callback) {
        // Supabase filter: selects user where username = given username
        String query = "eq." + username;
        // Send the request asynchronously and handle result via callback
        SupabaseClient.getApi().getUserByUsername(query).enqueue(callback);
    }
}
