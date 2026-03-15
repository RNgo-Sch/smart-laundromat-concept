package com.example.smart_laundromat_concept.data.session;

/**
 * Singleton class to manage the current user session.
 * Stores the logged-in user's information so it can be accessed from any screen.
 */
public class UserSession {
    private static UserSession instance;
    private String username;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void logout() {
        this.username = null;
    }
}
