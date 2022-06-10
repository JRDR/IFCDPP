package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.PaymentStatusModel;
import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.models.ReviewRequest;
import com.ifcdpp.ifcdpp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final UserService userService;

    @GetMapping("/review")
    public String getReviewForm(@RequestParam Long productId, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(email);

        Product product = productService.getProductById(productId);
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) == 0
                || paymentService.checkPaymentFromDb(productId, user.getId()) == PaymentStatusModel.SUCCESS) {
            model.addAttribute("product", product);
            return "review";
        }
        throw new MessageException("No successful payments on this product");
    }

    @PostMapping("/review")
    public String saveReview(ReviewRequest review) {
        reviewService.saveReview(review);
        return "redirect:/product/" + review.getProductId();
    }

}
