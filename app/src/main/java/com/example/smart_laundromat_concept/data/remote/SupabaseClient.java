package com.example.smart_laundromat_concept.data.remote;

import com.example.smart_laundromat_concept.BuildConfig;
import com.example.smart_laundromat_concept.data.model.AppMachine; // Updated to AppMachine
import com.example.smart_laundromat_concept.data.model.User;
import com.example.smart_laundromat_concept.data.model.Notification;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;
import retrofit2.Call;
import java.util.List;
import java.util.Map;

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
                "Range: 0-0"
        })
        Call<Void> getUserCount();

        @GET("machine")
        Call<List<AppMachine>> getMachineByStoreType(
                @Query("store") Integer storeNumber,
                @Query("type") String machineType
        );

        @GET("machine")
        Call<List<AppMachine>> getMachineByStoreTypeStatus(
                @Query("store") Integer storeNumber,
                @Query("type") String machineType,
                @Query("status") String machineStatus
        );

        @GET("notifications")
        Call<List<Notification>> getNotificationsByUser(
                @Query("user_id") String userIdQuery,
                @Query("order")   String order
        );

        /*
        @GET("queue") Call<List<Queue>> getActiveQueue(
                @Query("store") Integer storeNumber,
                @Query("type") String machineType
        );
        */

        @PATCH("users")
        @Headers({
                "Prefer: return=minimal"
        })
        Call<Void> updateWallet(
                @Query("id") String userIdQuery,
                @Body Map<String, Object> fields
        );
    }

    public static UserApi getApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    okhttp3.Request request = chain.request().newBuilder()
                            .addHeader("apikey", API_KEY)
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi.class);
    }
}
