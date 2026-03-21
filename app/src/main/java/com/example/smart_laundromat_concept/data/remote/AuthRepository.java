package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.data.model.User;
import retrofit2.Callback;
import java.util.List;

public class AuthRepository {

    /**
     * Queries Supabase for a user by username.
     * The Activity handles the response via the callback.
     *
     * @param username the username to search for
     * @param callback the Retrofit callback to handle success/failure in the Activity
     */
    public static void login(String username, Callback<List<User>> callback) {
        String query = "eq." + username;
        SupabaseClient.getApi().getUserByUsername(query).enqueue(callback);
    }

    /**
     * Communicates with Supabase to create a new user account.
     *
     * @param user     the user object containing credentials
     * @param callback the Retrofit callback to handle success/failure in the Activity
     */
    public static void signup(User user, Callback<List<User>> callback) {
        SupabaseClient.getApi().createUser(user).enqueue(callback);
    }
}
