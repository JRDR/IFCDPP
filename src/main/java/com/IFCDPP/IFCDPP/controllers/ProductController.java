package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product";
    }

    @PostMapping(path = "/product", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveProduct(Product product) {
        productService.saveProduct(product);
        return "redirect:/catalog";
    }

}
