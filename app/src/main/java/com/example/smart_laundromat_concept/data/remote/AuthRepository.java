package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.data.model.User;
import retrofit2.Callback;
import java.util.List;

/**
 * Handles authentication logic and data validation.
 * Centralizes communication with the Supabase backend.
 */
public class AuthRepository {

    /**
     * Validates login credentials.
     * TODO: Replace with actual Supabase API call.
     */
    public static boolean login(String username, String password) {
        User user = (User) SupabaseClient.getApi().getUserByUsername(username);
        return username.equals("user") && password.equals("1234");

    }

    /**
     * Communicates with Supabase to create a new user account.
     * 
     * @param user     The user object containing credentials.
     * @param callback The retrofit callback to handle success/failure in the Activity.
     */
    public static void signup(User user, Callback<List<User>> callback) {
        // Delegate the network call to the SupabaseClient
        SupabaseClient.getApi().createUser(user).enqueue(callback);
    }
}
