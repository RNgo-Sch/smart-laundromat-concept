package com.example.smart_laundromat_concept.data.remote.server;

import com.example.smart_laundromat_concept.data.model.QueueResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;

import java.util.Map;

public class BackendClient {

    // Automatically picks the right URL based on device type
    private static final String BASE_URL = isEmulator()
            ? "http://10.0.2.2:8080/" // for emulator
            : "http://192.168.1.111:8080/"; // jh's pc id

    private static boolean isEmulator() {
        return android.os.Build.FINGERPRINT.contains("generic")
                || android.os.Build.FINGERPRINT.contains("emulator")
                || android.os.Build.MODEL.contains("Android SDK")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.HARDWARE.contains("goldfish")
                || android.os.Build.HARDWARE.contains("ranchu");
    }
    public interface BackendApi {

        @GET("/washer/join")
        Call<QueueResponse> joinWasher(
                @Query("userId") String userId
        );

        @GET("/dryer/join")
        Call<QueueResponse> joinDryer(
                @Query("userId") String userId
        );


        @GET("queue/position")
        Call<QueueResponse> getPosition(
                @Query("userId") String userId,
                @Query("type") String machineType
        );

        @GET("/washer/cancel")
        Call<QueueResponse> cancelWasher
                (@Query("userId") String userId);

        @GET("/dryer/cancel")
        Call<QueueResponse> cancelDryer
                (@Query("userId") String userId);

        @GET("queue/next")
        Call<QueueResponse> nextUser(@Query("type") String machineType);

        @GET("queue/machines")
        Call<Map<String, Map<Integer, String>>> getMachines();
    }

    public static BackendApi getApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // JSON now
                .build()
                .create(BackendApi.class);
    }
}