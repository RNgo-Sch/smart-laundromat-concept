package com.example.smart_laundromat_concept.data.remote.repository;

import com.example.smart_laundromat_concept.data.model.User;
import com.example.smart_laundromat_concept.data.remote.supabase.SupabaseClient;

import java.util.List;

import retrofit2.Callback;

/**
 * Repository for all authentication-related network operations.
 *
 * <p>This class acts as the single point of contact between the auth Activities
 * and the Supabase backend. It constructs the correct query format and fires the
 * request, then hands the raw response back to the caller via a Retrofit
 * {@link Callback} — keeping network logic out of the Activities.
 *
 * <p>This class contains only static methods and holds no state.
 */
public class AuthRepository {

    /**
     * Fetches a user from Supabase by username.
     *
     * <p>Used during login. The caller's callback receives a list of matching users.
     * In practice only one user should match, so the caller should take
     * {@code response.body().get(0)}.
     *
     * @param username the username entered by the user
     * @param callback Retrofit callback that handles the response in the Activity
     */
    public static void login(String username, Callback<List<User>> callback) {
        // Supabase filter syntax: "eq.<value>" means WHERE username = '<value>'
        String query = "eq." + username;
        SupabaseClient.getApi().getUserByUsername(query).enqueue(callback);
    }

    /**
     * Creates a new user account in Supabase.
     *
     * <p>Used during sign-up. On success, Supabase returns the newly created user
     * row so it can be stored in {@link com.example.smart_laundromat_concept.data.session.UserSession}.
     *
     * @param user     the new user object containing username and password
     * @param callback Retrofit callback that handles the response in the Activity
     */
    public static void signup(User user, Callback<List<User>> callback) {
        SupabaseClient.getApi().createUser(user).enqueue(callback);
    }
}