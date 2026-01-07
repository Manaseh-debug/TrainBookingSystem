package com.trainbooking.auth;

import org.springframework.stereotype.Component;

@Component
public class LoginManager {

    public String login(String username, String password) {
        // Simple login simulation - return userID if valid, null if invalid
        if (username.equals("user1") && password.equals("pass123")) {
            return "user1";
        }
        return null;
    }
}

