package com.example.smart_laundromat_concept.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a single notification entity retrieved from the Supabase backend.
 * <p>
 * This class acts as a data model (Model layer) that maps directly to a row in the
 * "notifications" table stored in Supabase.
 *
 * <p><b>Responsibilities:</b>
 * <ul>
 *     <li>Stores notification-related data such as message, type, and timestamps.</li>
 *     <li>Provides getter methods for safe data access.</li>
 *     <li>Supports JSON deserialization via Retrofit + Gson.</li>
 * </ul>
 *
 * <p><b>Design Notes:</b>
 * <ul>
 *     <li>Uses {@link Type} enum to provide type-safe handling of notification categories.</li>
 *     <li>Uses {@link SerializedName} to map JSON fields from backend to Java fields.</li>
 *     <li>This class contains no UI logic, following separation of concerns.</li>
 * </ul>
 */
public class Notification {

    /**
     * Defines the different categories of notifications supported by the system.
     * <p>
     * Each enum value corresponds to a string identifier returned by the backend.
     * These identifiers must match exactly with the database values in Supabase.
     *
     * <p>Example:
     * <pre>
     * {
     *   "type": "booking"
     * }
     * </pre>
     *
     * <p>This enum provides:
     * <ul>
     *     <li>Type safety (avoids raw string comparison errors)</li>
     *     <li>Centralized definition of valid notification types</li>
     * </ul>
     */
    public enum Type {
        BOOKING("booking"),
        CYCLE_DONE("cycle_done"),
        QUEUE("queue"),
        SYSTEM("system");

        private final String value;

        /**
         * Constructs a notification type with its corresponding backend string value.
         *
         * @param value the string representation used by the backend (e.g., "booking")
         */
        Type(String value) {
            this.value = value;
        }

        /**
         * Returns the string value associated with this enum.
         * <p>
         * This is used when sending data back to the backend or comparing raw values.
         *
         * @return backend string value of the type
         */
        public String getValue() {
            return value;
        }

        /**
         * Converts a raw string value from the backend into a corresponding enum type.
         * <p>
         * This method is useful when parsing JSON responses where "type" is a string.
         *
         * @param value the raw string value from backend
         * @return matching {@link Type}, or SYSTEM as a fallback if no match is found
         */
        public static Type fromString(String value) {
            for (Type t : Type.values()) {
                if (t.value.equals(value)) {
                    return t;
                }
            }
            return SYSTEM; // fallback
        }
    }

    // -------------------------------------------------------------------------
    // Fields (mapped from backend JSON)
    // -------------------------------------------------------------------------

    // Unique identifier of the notification (primary key in database)
    private int id;

    // The content/message of the notification displayed to the user
    private String message;

    // Raw type string received from backend (to be converted using Type enum)
    private String type;

    // Timestamp indicating when the notification was created (ISO format)
    @SerializedName("created_at")
    private String createdAt;

    // Indicates whether the user has read the notification
    @SerializedName("is_read")
    private boolean isRead;

    // ID of the user associated with this notification
    @SerializedName("user_id")
    private int userId;

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the notification ID.
     *
     * @return unique notification identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the notification message.
     *
     * @return message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the raw notification type string.
     * <p>
     * Use {@link Type#fromString(String)} to convert it into enum form.
     *
     * @return notification type as string
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the notification type as an enum.
     * <p>
     * This converts the raw string value into a type-safe {@link Type}.
     * Recommended for use in logic instead of {@link #getType()}.
     *
     * @return notification type as enum
     */
    public Type getTypeEnum() {
        return Type.fromString(type);
    }

    /**
     * Returns the timestamp when the notification was created.
     *
     * @return creation timestamp
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Indicates whether the notification has been read.
     *
     * @return true if read, false otherwise
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Returns the ID of the user associated with this notification.
     *
     * @return user ID
     */
    public int getUserId() {
        return userId;
    }
}