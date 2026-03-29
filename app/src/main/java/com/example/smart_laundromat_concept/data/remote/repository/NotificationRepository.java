package com.example.smart_laundromat_concept.data.remote.repository;

import com.example.smart_laundromat_concept.data.model.Notification;
import com.example.smart_laundromat_concept.data.remote.supabase.SupabaseClient;
import com.example.smart_laundromat_concept.data.session.UserSession;

import java.util.List;

import retrofit2.Callback;

/**
 * Handles fetching notifications from Supabase for the current user.
 */
public class NotificationRepository {

    /**
     * Fetches all notifications for the logged-in user, newest first.
     *
     * @param callback the Retrofit callback to handle success/failure
     */
    public static void getNotifications(Callback<List<Notification>> callback) {
        int userId = UserSession.getInstance().getCurrentUser().getId();
        String userIdQuery = "eq." + userId;

        SupabaseClient.getApi()
                .getNotificationsByUser(userIdQuery, "created_at.desc")
                .enqueue(callback);
    }
}