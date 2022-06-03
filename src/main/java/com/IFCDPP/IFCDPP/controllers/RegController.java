package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.models.RegistrationModel;
import com.ifcdpp.ifcdpp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegController {

    private final UserService userService;

    @GetMapping("/reg")
    public String regPage(Model model) {
        model.addAttribute("title", "Регистрация");
        return "reg";
    }

    @PostMapping(path = "/reg")
    public String register(RegistrationModel registrationModel) {
        boolean isRegistered = userService.registerUser(registrationModel);
        if (!isRegistered) {
            return "reg";
        }
        return "login";
    }
}
