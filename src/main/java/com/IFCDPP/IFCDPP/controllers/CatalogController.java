package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;

    @GetMapping("/catalog")
    public String getCatalog(@RequestParam(required = false) Integer page,
                             @RequestParam(required = false) Long categoryId, Model model) {
        model.addAttribute("catalog", productService.getCatalogOnPage(page, categoryId));
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("currentCategory", categoryId);
        return "catalog";
    }

    @GetMapping("/catalog/add")
    public String catalogAdd(Model model){
        return "catalog-add";
    }

}
