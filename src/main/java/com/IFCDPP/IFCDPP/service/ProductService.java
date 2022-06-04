package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.ProductEntity;
import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getFullCatalog() {
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream().map(this::mapEntityForCatalog).collect(Collectors.toList());
    }

    private Product mapEntityForCatalog(ProductEntity entity) {
        return Product.builder().title(entity.getTitle()).description(entity.getDescription())
                .developer(entity.getDeveloper()).price(entity.getPrice()).downloadLink(entity.getDownloadLink())
                .build();
    }

}
