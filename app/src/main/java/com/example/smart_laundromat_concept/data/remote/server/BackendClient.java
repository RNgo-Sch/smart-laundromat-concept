package com.example.smart_laundromat_concept.data.remote.server;

import com.example.smart_laundromat_concept.data.model.QueueResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.Call;

import java.util.Map;

public class BackendClient {
    private static final String BASE_URL =
            isEmulator()
                    ? "http://10.0.2.2:3000/" // for local host address
                    : "https://laundromat-server-production.up.railway.app/";

    private static boolean isEmulator() {
        return android.os.Build.FINGERPRINT.contains("generic")
                || android.os.Build.FINGERPRINT.contains("emulator")
                || android.os.Build.MODEL.contains("Android SDK")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.HARDWARE.contains("goldfish")
                || android.os.Build.HARDWARE.contains("ranchu");
    }
    public interface BackendApi {

        @POST("/queue/washer")
        Call<Void> joinWasher(@Query("userId") String userId);

        @POST("/queue/dryer")
        Call<Void> joinDryer(@Query("userId") String userId);


        @GET("queue/position")
        Call<QueueResponse> getPosition(
                @Query("userId") String userId,
                @Query("type") String machineType
        );

        @POST("/interact")
        Call<Void> interact(
                @Query("userId") int userId,
                @Query("machineId") int machineId
        );

        @POST("/leave/washer")
        Call<Void> leaveWasher(@Query("userId") String userId);

        @POST("/leave/dryer")
        Call<Void> leaveDryer(@Query("userId") String userId);

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