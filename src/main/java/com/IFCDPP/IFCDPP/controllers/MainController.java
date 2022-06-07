package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Main Page");
        model.addAttribute("map", productService.getProductsForMain());
        return "home";
    }

    @GetMapping("/contacts")
    public String contacts(Model model) {
        model.addAttribute("title", "Поддержка");
        return "contacts";
    }

    @GetMapping("/devinfo")
    public String devInfo(Model model) {
        model.addAttribute("title", "Поддержка");
        return "devinfo";
    }

}