package com.example.smart_laundromat_concept.data.model;

import com.example.smart_laundromat_concept.R;

/**
 * Represents a laundry machine in the smart laundromat system.
 * <p>
 * This class serves as a data model (Model layer) that stores:
 * <ul>
 *     <li>Machine ID</li>
 *     <li>Current operational state</li>
 *     <li>Machine type (e.g., Washer, Dryer)</li>
 * </ul>
 *
 * <p><b>Design Notes:</b>
 * <ul>
 *     <li>Uses {@link State} enum to ensure type-safe state management.</li>
 *     <li>Separates UI concerns by storing only a string resource ID instead of raw text.</li>
 *     <li>Designed to integrate with backend systems (e.g., Spring Boot, Supabase).</li>
 * </ul>
 */
public class AppMachine {

    /**
     * Represents the current state of a laundry machine.
     * <p>
     * Each state is associated with a string resource ID, allowing the UI layer
     * to display localized text without hardcoding values in the model.
     */
    public enum State {
        AVAILABLE(R.string.status_open),
        RESERVED(R.string.status_reserved),
        IN_USE(R.string.status_in_use),
        COLLECTION(R.string.status_collection),
        OOS(R.string.status_out_of_service);

        private final int stringResId;

        /**
         * Constructs a State enum value and associates it with a string resource ID.
         * <p>
         * This resource ID is later used by the UI layer to retrieve a localized
         * string from {@code strings.xml}, ensuring proper separation of logic and presentation.
         *
         * @param stringResId the string resource ID representing this machine state
         */
        State(int stringResId) {
            this.stringResId = stringResId;
        }

        /**
         * Returns the string resource ID associated with this state.
         * <p>
         * The UI layer should call {@code context.getString(getStringResId())}
         * to obtain the human-readable, localized text.
         *
         * @return the string resource ID for this state
         */
        public int getStringResId() {
            return stringResId;
        }
    }

    // Unique identifier for the machine (e.g., 1, 2, 3, ...)
    private final int id;
    // Current operational state of the machine
    private State state;
    // Type of machine (e.g., "Washer", "Dryer")
    private final String type;

    /**
     * Constructs a new AppMachine instance.
     *
     * @param id    unique machine identifier
     * @param state initial state of the machine
     * @param type  type of machine (e.g., Washer or Dryer)
     */
    public AppMachine(int id, State state, String type) {
        this.id = id;         // Assign machine ID
        this.state = state;   // Set initial state
        this.type = type;     // Set machine type
    }

    /**
     * Returns the unique ID of the machine.
     *
     * @return machine ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the current state of the machine.
     *
     * @return current {@link State}
     */
    public State getState() {
        return this.state;
    }

    /**
     * Returns the type of the machine.
     *
     * @return machine type (e.g., Washer or Dryer)
     */
    public String getMachineType() {
        return this.type;
    }

    /**
     * Updates the state of the machine.
     *
     * @param newState the new state to set
     */
    public void changeState(State newState) {
        this.state = newState; // Update machine state
    }
}