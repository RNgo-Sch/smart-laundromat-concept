package com.example.smart_laundromat_concept.classes;

import com.example.smart_laundromat_concept.BuildConfig;
import com.google.gson.internal.GsonBuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;
import retrofit2.Call;
import java.util.List;

public class SupabaseClient {

    private static final String BASE_URL = "https://timticjbrpcmkdwugrtd.supabase.co";

    private static final String API_KEY = BuildConfig.Supabase_Key;

    public interface UserApi {
        // Create User: POST to /users
        @POST("users")
        @Headers({
                "apikey: " + API_KEY,
                "Authorization: Bearer " + API_KEY,
                "Content-Type: application/json",
                "Prefer: return=representation"
        })
        Call<List<User>> createUser(@Body User user);

        // Query User: GET from /users?username=eq.value
        @GET("users")
        @Headers({
                "apikey: " + API_KEY,
                "Authorization: Bearer " + API_KEY
        })
        Call<List<User>> getUserByUsername(@Query("username") String username);
    }

    public static UserApi getApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi.class);
    }
}
