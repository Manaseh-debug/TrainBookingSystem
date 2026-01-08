package com.trainbooking.auth;

import com.trainbooking.bookingcomponent.interfaces.IAuthService;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceImpl implements IAuthService {

    @Override
    public boolean isValidUser(String userID) {

        return userID != null && !userID.isEmpty();
    }
}

