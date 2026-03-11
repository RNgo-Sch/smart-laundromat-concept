package com.example.smart_laundromat_concept.classes;

import android.util.Log;
import android.widget.Toast;

import com.example.smart_laundromat_concept.page.SignUpActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// this class exists to provide sequential id numbers for other objects
public class IdCounter {
    private int current_id;

    public IdCounter() {
        current_id = 0;
    }

    public int getId() {
        SupabaseClient.getApi().getUserCount().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // The count is returned in the "Content-Range" header
                    // Format: "0-0/15" where 15 is the total count
                    String rangeHeader = response.headers().get("Content-Range");
                    if (rangeHeader != null && rangeHeader.contains("/")) {
                        String countStr = rangeHeader.substring(rangeHeader.indexOf("/") + 1);
                        current_id = Integer.parseInt(countStr);

                        Log.d("DEBUG", "current_id successfully saved as: " + current_id);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SUPABASE_COUNT", "Error: " + t.getMessage());
            }
        });
        return current_id;
    }
}