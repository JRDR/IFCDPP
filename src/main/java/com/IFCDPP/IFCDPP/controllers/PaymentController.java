package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/purchase/{id}")
    public String purchaseProduct(@PathVariable("id") Long productId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return "redirect:" + paymentService.createForm(email, productId);
    }

    @GetMapping("/cancel/{id}")
    public String cancelPurchase(@PathVariable("id") Long productId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        paymentService.cancelPayment(email, productId);
        return "redirect:/product/" + productId;
    }

    @GetMapping("/checkPayment")
    public String checkPayment(@RequestParam("productId") Long productId, @RequestParam("paymentId") String paymentId, Model model) {
        paymentService.checkPayment(paymentId);
        return "redirect:/product/" + productId;
    }

}
