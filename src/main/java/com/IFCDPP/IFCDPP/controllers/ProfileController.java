package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable("id") Long userId, Model model) {
        model.addAttribute("user", userService.getUserProfile(userId));
        return "profile";
    }

}