package com.example.smart_laundromat_concept.data.remote.server;

import com.example.smart_laundromat_concept.data.model.QueueResponse;

public interface QueueCallback {
    void onSuccess(QueueResponse response);
    void onError(String error);
}
