package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.ProductEntity;
import com.ifcdpp.ifcdpp.entity.ReviewEntity;
import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.PaymentStatusModel;
import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.models.Review;
import com.ifcdpp.ifcdpp.models.ReviewRequest;
import com.ifcdpp.ifcdpp.repo.ProductRepository;
import com.ifcdpp.ifcdpp.repo.ReviewRepository;
import com.ifcdpp.ifcdpp.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;

    public List<Review> getAllReviewsByUser(Long userId) {
        return reviewRepository.findAllByUser_Id(userId).stream().map(this::mapReview).collect(Collectors.toList());
    }

    public void saveReview(ReviewRequest review) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new MessageException("User not found"));

        ProductEntity product = productRepository.findById(review.getProductId())
                .orElseThrow(() -> new MessageException("Product not found"));
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) == 0
                || paymentService.checkPaymentFromDb(review.getProductId(), user.getId()) == PaymentStatusModel.SUCCESS) {
            ReviewEntity entity = new ReviewEntity();
            entity.setTopic(review.getTopic());
            entity.setDescription(review.getDescription());
            entity.setProduct(product);
            entity.setUser(user);

            reviewRepository.save(entity);
        } else {
            throw new MessageException("No successful payments on this product");
        }
    }

    private Review mapReview(ReviewEntity entity) {
        return Review.builder().id(entity.getId()).topic(entity.getTopic()).description(entity.getDescription())
                .product(mapProductForReview(entity.getProduct())).build();
    }

    private Product mapProductForReview(ProductEntity entity) {
        return Product.builder().id(entity.getId()).title(entity.getTitle()).build();
    }

}
