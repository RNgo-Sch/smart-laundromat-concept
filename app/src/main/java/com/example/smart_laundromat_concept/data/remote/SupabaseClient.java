package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.BuildConfig;
import com.example.smart_laundromat_concept.data.model.Machine;
import com.example.smart_laundromat_concept.data.model.User;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;
import retrofit2.Call;
import java.util.List;

public class SupabaseClient {

    private static final String BASE_URL = "https://timticjbrpcmkdwugrtd.supabase.co/rest/v1/";

    private static final String API_KEY = BuildConfig.Supabase_Key;

    public interface UserApi {
        @POST("users")
        @Headers("Prefer: return=representation")
        Call<List<User>> createUser(@Body User user);

        @GET("users")
        Call<List<User>> getUserByUsername(@Query("username") String usernameQuery);

        @GET("users")
        @Headers({
                "Prefer: count=exact",
                "Range: 0-0" //Give count not rows
        })
        Call<Void> getUserCount();

        @GET("machine")
        Call<List<Machine>> getMachineByStoreType(
                @Query("store") Integer storeNumber,
                @Query("type") String machineType,
                @Query("status") String curStatus
        );

        /*
        Temp: extra feature
        @GET("store")
        Call<List<Store>> getStoreByNumber(
                @Query("name") String storeName
        );
         */

        /*
        TODO: Get Queue information
        TODO: Post new Queue user
        TODO: Post update Queue user
        TODO: Get Store
        TODO: Get Machine from Store
        TODO: Get available Machines
         */
    }

    public static UserApi getApi() {
        // 1. Create a client that adds the headers automatically
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    okhttp3.Request request = chain.request().newBuilder()
                            .addHeader("apikey", API_KEY)
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        // 2. Build Retrofit using that client
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi.class);
    }
}
