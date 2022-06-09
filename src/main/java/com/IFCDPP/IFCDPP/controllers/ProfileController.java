package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.models.Profile;
import com.ifcdpp.ifcdpp.service.PaymentService;
import com.ifcdpp.ifcdpp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final PaymentService paymentService;

    @GetMapping("/profile")
    public String getCurrentProfile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Profile profile = userService.getUserProfile(email);
        model.addAttribute("user", profile);
        model.addAttribute("payments", paymentService.getPaymentsByUserId(profile.getId()));
        model.addAttribute("showModerate", false);
        return "profile";
    }

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable(name = "id", required = false) Long userId, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Profile profile = userService.getUserProfile(email);
        if (profile.getId().equals(userId)) return "redirect:/profile";
        model.addAttribute("user", userService.getUserProfile(userId));
        model.addAttribute("showModerate", true);
        return "profile";
    }

}