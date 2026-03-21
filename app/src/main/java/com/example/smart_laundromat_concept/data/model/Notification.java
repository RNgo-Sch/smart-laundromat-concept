package com.example.smart_laundromat_concept.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a single notification entry from the Supabase notifications table.
 */
public class Notification {

    public static final String TYPE_BOOKING   = "booking";
    public static final String TYPE_CYCLE_DONE = "cycle_done";
    public static final String TYPE_QUEUE      = "queue";
    public static final String TYPE_SYSTEM     = "system";

    private int id;
    private String message;
    private String type;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("is_read")
    private boolean isRead;

    @SerializedName("user_id")
    private int userId;

    public int getId()          { return id; }
    public String getMessage()  { return message; }
    public String getType()     { return type; }
    public String getCreatedAt(){ return createdAt; }
    public boolean isRead()     { return isRead; }
    public int getUserId()      { return userId; }
}