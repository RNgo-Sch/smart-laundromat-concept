package com.example.smart_laundromat_concept.data.model;

import static com.example.smart_laundromat_concept.data.model.AppMachine.State.*;

import java.util.HashMap;
import java.util.Map;

public class MachineData {

    private static final Map<Integer, AppMachine.State> washers = new HashMap<>();
    private static final Map<Integer, AppMachine.State> dryers  = new HashMap<>();


    static {
        // Washer initial states
        washers.put(1, RESERVED);
        washers.put(2, IN_USE);
        washers.put(3, AVAILABLE);
        washers.put(4, OOS);

        // Dryer initial states
        dryers.put(1, AVAILABLE);
        dryers.put(2, COLLECTION);
        dryers.put(3, OOS);
        dryers.put(4, IN_USE);

    }

    // ------------------- Washer -------------------
    public static void setWasherState(int id, AppMachine.State state) {
        washers.put(id, state);
    }

    public static Map<Integer, AppMachine.State> getWashers() {
        return washers;
    }

    // ------------------- Dryer -------------------
    public static void setDryerState(int id, AppMachine.State state) {
        dryers.put(id, state);
    }

    public static Map<Integer, AppMachine.State> getDryers() {
        return dryers;
    }
}