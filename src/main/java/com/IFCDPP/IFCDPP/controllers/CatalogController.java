package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;

    @GetMapping("/catalog")
    public String getCatalog(Model model) {
        model.addAttribute("products", productService.getFullCatalog());
        return "catalog";
    }

}
