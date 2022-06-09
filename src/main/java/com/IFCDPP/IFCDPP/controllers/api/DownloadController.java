package com.ifcdpp.ifcdpp.controllers.api;

import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.PaymentStatusModel;
import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.service.FileService;
import com.ifcdpp.ifcdpp.service.PaymentService;
import com.ifcdpp.ifcdpp.service.ProductService;
import com.ifcdpp.ifcdpp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class DownloadController {

    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    private final ProductService productService;
    private final UserService userService;
    private final FileService fileService;
    private final PaymentService paymentService;

    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("productId") Long productId, HttpServletRequest request) {
        Product product = productService.getProductById(productId);
        Resource resource = fileService.loadFileAsResource(product.getDownloadLink());

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(email);
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) == 0 ||
                paymentService.checkPaymentFromDb(productId, user.getId()) == PaymentStatusModel.SUCCESS) {
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                logger.info("Could not determine file type.");
            }

            if(contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }

        throw new MessageException("No payment information or payment is still pending");
    }
}
