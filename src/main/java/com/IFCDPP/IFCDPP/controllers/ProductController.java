package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.models.PaymentStatusModel;
import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.models.ProductRequest;
import com.ifcdpp.ifcdpp.service.FileService;
import com.ifcdpp.ifcdpp.service.PaymentService;
import com.ifcdpp.ifcdpp.service.ProductService;
import com.ifcdpp.ifcdpp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;
    private final PaymentService paymentService;
    private final UserService userService;

    @GetMapping("/catalog")
    public String getCatalog(@RequestParam(required = false) Integer page,
                             @RequestParam(required = false, defaultValue = "") String title,
                             @RequestParam(required = false) Long categoryId, Model model) {
        model.addAttribute("catalog", productService.getCatalogOnPage(page, title, categoryId));
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("currentCategory", categoryId);
        model.addAttribute("currentQuery", title);
        return "catalog";
    }

    @GetMapping("/catalog/add")
    public String catalogAdd(Model model){
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("product", new Product());
        return "catalog-add";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model){
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("product", productService.getProductById(id));
        return "catalog-add";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(email);
        if (user == null) {
            model.addAttribute("paymentStatus", PaymentStatusModel.CANCELLED);
        } else {
            model.addAttribute("paymentStatus", paymentService.checkPaymentFromDb(id, user.getId()));
        }

        model.addAttribute("product", productService.getProductById(id));

        return "product";
    }

    @PostMapping( "/product")
    public String saveProduct(ProductRequest product) {
        Long newId = productService.saveProduct(product);
        if (product.getFile().getSize() != 0) {
            String fileName = fileService.storeFile(product.getFile());
            productService.addDownloadLinkToProduct(fileName, newId);
        }
        return "redirect:/product/" + newId;
    }

    @PostMapping( "/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        paymentService.cancelAllPaymentsOnProducts(productId);
        productService.deleteProduct(productId);
        return "redirect:/catalog";
    }

}
