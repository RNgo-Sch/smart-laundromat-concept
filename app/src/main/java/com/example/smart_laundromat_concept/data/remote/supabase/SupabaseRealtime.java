package com.example.smart_laundromat_concept.data.remote.supabase;

import android.util.Log;

import com.example.smart_laundromat_concept.BuildConfig;
import com.example.smart_laundromat_concept.data.remote.repository.MachineRepository;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SupabaseRealtime {

    private static final String WS_URL =
            "wss://timticjbrpcmkdwugrtd.supabase.co/realtime/v1/websocket?apikey="
                    + BuildConfig.Supabase_Key;

    public interface MachineUpdateListener {
        void onMachineUpdated(int id, String type, int position, String status, Integer currentUser);
    }

    public static void subscribeToMachines() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(WS_URL)
                .build();

        WebSocket ws = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("REALTIME", "Connected");

                // Subscribe to machine table
                String payload = "{\n" +
                        "  \"topic\": \"realtime:public:machine\",\n" +
                        "  \"event\": \"phx_join\",\n" +
                        "  \"payload\": {},\n" +
                        "  \"ref\": \"1\"\n" +
                        "}";

                webSocket.send(payload);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    JSONObject json = new JSONObject(text);

                    if (!json.getString("event").equals("UPDATE")) return;

                    JSONObject record = json
                            .getJSONObject("payload")
                            .getJSONObject("record");

                    int id = record.getInt("id");
                    String type = record.getString("type");
                    String status = record.getString("status");
                    int position = record.getInt("position");
                    Integer currentUser = record.isNull("current_user")
                            ? null
                            : record.getInt("current_user");

                    // ✅ Only update if changed
                    String currentStatus = MachineRepository.getMachineStatus(id);

                    if (currentStatus == null || !currentStatus.equals(status)) {
                        MachineRepository.updateMachineLocally(type, position, status, currentUser);

                        if (listener != null) {
                            listener.onMachineUpdated(id, type, position, status, currentUser);
                        }

                        Log.d("REALTIME", "Updated machine " + id + " -> " + status);
                    } else {
                        Log.d("REALTIME", "Skipped duplicate update for machine " + id);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Only one MachineUpdateListener interface should exist, defined above.

    private static MachineUpdateListener listener;

    public static void setListener(MachineUpdateListener l) {
        listener = l;
    }
}
