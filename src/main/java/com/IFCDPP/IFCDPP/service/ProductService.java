package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.CategoryEntity;
import com.ifcdpp.ifcdpp.entity.ProductEntity;
import com.ifcdpp.ifcdpp.entity.ReviewEntity;
import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.*;
import com.ifcdpp.ifcdpp.repo.CategoryRepository;
import com.ifcdpp.ifcdpp.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Catalog getCatalogOnPage(Integer page, String query, Long categoryId) {
        if (page == null) {
            page = 1;
        }

        Pageable pageWithFiveElems = PageRequest.of(page - 1, 5);
        Page<ProductEntity> entities;
        if (categoryId == null) {
            entities = productRepository.findAllByTitleStartsWithIgnoreCase(query, pageWithFiveElems);
        } else {
            entities = productRepository.findAllByCategory_IdAndTitleStartsWithIgnoreCase(categoryId, query, pageWithFiveElems);
        }
        List<Product> products = entities.stream().map(this::mapProductForCatalog).collect(Collectors.toList());
        int pagesNum = entities.getTotalPages();

        return new Catalog(products, pagesNum);
    }

    public Map<Category, List<Product>> getProductsForMain() {
        Pageable pageWithFourElems = PageRequest.of(0, 4);
        Pageable pageWithThreeElems = PageRequest.of(0, 4);
        Page<CategoryEntity> categories = categoryRepository.findAll(pageWithFourElems);

        Map<Category, List<Product>> productsWithCategories = new HashMap<>();
        for (CategoryEntity category : categories) {
            Page<ProductEntity> products = productRepository.findAllByCategory_Id(category.getId(), pageWithThreeElems);
            productsWithCategories.put(mapCategory(category),
                    products.stream().map(this::mapProductForCatalog).collect(Collectors.toList()));
        }

        return productsWithCategories;
    }

    public Product getProductById(Long id) {
        Optional<ProductEntity> entity = productRepository.findById(id);
        return entity.map(this::mapProduct).orElse(null);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mapCategory).collect(Collectors.toList());
    }

    public Long saveProduct(ProductRequest product) {
        ProductEntity entity = productRepository.findById(product.getId() == null ? -1 : product.getId())
                .orElse(new ProductEntity());

        entity.setTitle(product.getTitle());
        entity.setDescription(product.getDescription());
        entity.setDeveloper(product.getDeveloper());
        entity.setPrice(product.getPrice());
        entity.setImageLink(product.getImageLink());
        entity.setPreferences(product.getPreferences());
        entity.setDeveloperLink(product.getDeveloperLink());

        Optional<CategoryEntity> category = categoryRepository.findByTitle(product.getCategory());
        if (category.isPresent()) {
            entity.setCategory(category.get());
        } else {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setTitle(product.getCategory());
            categoryEntity = categoryRepository.save(categoryEntity);
            entity.setCategory(categoryEntity);
        }

        return productRepository.save(entity).getId();

    }

    public void deleteProduct(Long productId) {
        ProductEntity entity = productRepository.findById(productId).orElseThrow(() -> new MessageException("Product not found"));
        productRepository.delete(entity);
    }

    public void addDownloadLinkToProduct(String downloadLink, Long id) {
        Optional<ProductEntity> optional = productRepository.findById(id);

        ProductEntity entity = optional.orElseThrow(() -> new MessageException("Product not found"));
        entity.setDownloadLink(downloadLink);

        productRepository.save(entity);
    }

    private Product mapProductForCatalog(ProductEntity entity) {
        return Product.builder().id(entity.getId()).title(entity.getTitle()).description(entity.getDescription())
                .category(entity.getCategory() == null ? "?????? ??????????????????" : entity.getCategory().getTitle())
                .developer(entity.getDeveloper()).imageLink(entity.getImageLink())
                .price(entity.getPrice()).developerLink(entity.getDeveloperLink())
                .build();
    }

    private Product mapProduct(ProductEntity entity) {
        return Product.builder().id(entity.getId()).title(entity.getTitle()).description(entity.getDescription())
                .developer(entity.getDeveloper()).price(entity.getPrice()).downloadLink(entity.getDownloadLink())
                .category(entity.getCategory() == null ? "?????? ??????????????????" : entity.getCategory().getTitle())
                .imageLink(entity.getImageLink()).developerLink(entity.getDeveloperLink())
                .preferences(entity.getPreferences())
                .reviews(entity.getReviews().stream().map(this::mapReview).collect(Collectors.toList()))
                .build();
    }

    private Category mapCategory(CategoryEntity entity) {
        return Category.builder().id(entity.getId()).title(entity.getTitle()).build();
    }

    private Review mapReview(ReviewEntity entity) {
        return Review.builder().id(entity.getId()).topic(entity.getTopic()).description(entity.getDescription())
                .user(mapUserForReview(entity.getUser())).build();
    }

    private IssueUser mapUserForReview(User user) {
        return IssueUser.builder().id(user.getId()).login(user.getLogin()).build();
    }

}
