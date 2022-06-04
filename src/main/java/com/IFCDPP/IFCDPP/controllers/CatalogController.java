package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;

    @GetMapping("/catalog")
    public String getCatalog(@RequestParam(required = false) Integer page, Model model) {
        model.addAttribute("products", productService.getFullCatalogOnPage(page));
        return "catalog";
    }

}
