package com.trainbooking.controllers;

import com.trainbooking.auth.LoginManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final LoginManager loginManager;

    public LoginController(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        String userID = loginManager.login(username, password);
        if (userID != null) {
            model.addAttribute("userID", userID);
            return "booking";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }
}
