package com.example.smart_laundromat_concept.ui.activities.main.utils;


import android.os.Handler;
import android.os.Looper;

public class PollingManager {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable task;
    private final long interval;

    private boolean isRunning = false;

    public PollingManager(Runnable task, long intervalMillis) {
        this.task = task;
        this.interval = intervalMillis;
    }

    private final Runnable internalRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;

            task.run();
            handler.postDelayed(this, interval);
        }
    };

    public void start() {
        if (isRunning) return;
        isRunning = true;
        handler.post(internalRunnable);
    }

    public void stop() {
        isRunning = false;
        handler.removeCallbacks(internalRunnable);
    }
}