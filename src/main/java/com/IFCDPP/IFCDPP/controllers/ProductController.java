package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.service.FileService;
import com.ifcdpp.ifcdpp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;

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
        return "catalog-add";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product";
    }

    @GetMapping("/product/{id}/upload")
    public String getUploadPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product-upload";
    }

    @PostMapping("/product/{id}/upload")
    public String uploadFile(@PathVariable("id") Long productId, @RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        productService.addDownloadLinkToProduct(fileDownloadUri, productId);

        return "redirect:/product/" + productId;
    }

    @PostMapping(path = "/product", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveProduct(Product product) {
        productService.saveProduct(product);
        return "redirect:/catalog";
    }

}
