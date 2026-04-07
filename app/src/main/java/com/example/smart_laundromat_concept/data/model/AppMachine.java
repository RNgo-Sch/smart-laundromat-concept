package com.example.smart_laundromat_concept.data.model;

import com.example.smart_laundromat_concept.R;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Unified machine model for the Smart Laundromat system.
 * <p>
 * Combines:
 * - Supabase data model (raw fields from database)
 * - UI state model (State enum with string resources)
 * - Static in-memory store (AppMachine)
 */
public class AppMachine {

    private String assignedUserId;
    public String getAssignedUserId() {
        if (assignedUserId != null) return assignedUserId;
        if (currentUser != null) return String.valueOf(currentUser);
        return null;
    }
    @SerializedName("position")
    public int position;


    // -------------------------------------------------------------------------
    // State Enum
    // -------------------------------------------------------------------------

    public enum State {
        AVAILABLE(R.string.status_open),
        RESERVED(R.string.status_reserved),
        IN_USE(R.string.status_in_use),
        COLLECTION(R.string.status_collection),
        OOS(R.string.status_out_of_service);

        private final int stringResId;


        State(int stringResId) {
            this.stringResId = stringResId;
        }

        public int getStringResId() {
            return stringResId;
        }

        /**
         * Converts a raw Supabase status string into a State enum.
         *
         * @param status raw string from Supabase e.g. "IN_USE"
         * @return matching State, defaults to AVAILABLE
         */
        public static State fromString(String status) {
            if (status == null) return AVAILABLE;
            switch (status.toUpperCase()) {
                case "RESERVED":   return RESERVED;
                case "IN_USE":     return IN_USE;
                case "COLLECTION": return COLLECTION;
                case "OOS":        return OOS;
                default:           return AVAILABLE;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Supabase Fields (mapped from JSON)
    // -------------------------------------------------------------------------

    /** Database ID — used for Supabase mapping. */
    public int id;

    /** Raw status string from Supabase e.g. "AVAILABLE", "IN_USE". */
    public String status;

    /** Machine type from Supabase e.g. "washer" or "dryer". */
    public String type;

    /** Store number this machine belongs to. */
    public Integer store;

    /** Username of the current user using this machine. */
    @SerializedName("current_user")
    public Integer currentUser;

    public void setCurrentUser(Integer userId) {
        this.currentUser = userId;
    }

    // -------------------------------------------------------------------------
    // UI State
    // -------------------------------------------------------------------------

    /** UI state derived from the raw status string. */
    private State state;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Default constructor — required for Gson deserialization from Supabase. */
    public AppMachine() {
        this.state = State.AVAILABLE;
    }

    /**
     * Constructs a machine with explicit values.
     * Used when creating machines manually (e.g. demo data).
     *
     * @param id    machine position number (1-4)
     * @param state initial UI state
     * @param type  "washer" or "dryer"
     */
    public AppMachine(int id, State state, String type) {
        this.id    = id;
        this.state = state;
        this.type  = type;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public int getId()            { return id; }
    public String getMachineType(){ return type; }

    /**
     * Returns the UI state.
     * If state has not been explicitly set, derives it from the raw status string.
     */
    public State getState() {
        if (state == null) {
            state = State.fromString(status);
        }
        return state;
    }

    /** Updates the UI state. */
    public void setState(State newState) {
        this.state  = newState;
        this.status = newState.name(); // keep raw status in sync
    }

    // -------------------------------------------------------------------------
    // Static In-Memory Store (replaces AppMachine)
    // -------------------------------------------------------------------------

    private static final Map<Integer, AppMachine> washers = new HashMap<>();
    private static final Map<Integer, AppMachine> dryers  = new HashMap<>();

    static {
        // Default machines — overwritten by Supabase fetch on app start
        for (int i = 1; i <= 4; i++) {
            washers.put(i, new AppMachine(i, State.AVAILABLE, "washer"));
            dryers.put(i, new AppMachine(i, State.AVAILABLE, "dryer"));
        }
    }

    public static void setWasherState(int id, State state, Integer userId) {
        if (washers.containsKey(id)) {
            AppMachine m = washers.get(id);
            m.setState(state);
            m.setCurrentUser(userId);
        }
    }

    public static void setDryerState(int id, State state, Integer userId) {
        if (dryers.containsKey(id)) {
            AppMachine m = dryers.get(id);
            m.setState(state);
            m.setCurrentUser(userId);
        }
    }

    public static Map<Integer, AppMachine> getWashers() { return washers; }
    public static Map<Integer, AppMachine> getDryers()  { return dryers; }
}