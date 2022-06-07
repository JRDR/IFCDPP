package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.CategoryEntity;
import com.ifcdpp.ifcdpp.entity.ProductEntity;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.Catalog;
import com.ifcdpp.ifcdpp.models.Category;
import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.models.ProductRequest;
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

    public Map<String, List<Product>> getProductsForMain() {
        Pageable pageWithFourElems = PageRequest.of(0, 4);
        Pageable pageWithThreeElems = PageRequest.of(0, 4);
        Page<CategoryEntity> categories = categoryRepository.findAll(pageWithFourElems);

        Map<String, List<Product>> productsWithCategories = new HashMap<>();
        for (CategoryEntity category : categories) {
            Page<ProductEntity> products = productRepository.findAllByCategory_Id(category.getId(), pageWithThreeElems);
            productsWithCategories.put(category.getTitle(),
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

    public void addDownloadLinkToProduct(String downloadLink, Long id) {
        Optional<ProductEntity> optional = productRepository.findById(id);

        ProductEntity entity = optional.orElseThrow(() -> new MessageException("Product not found"));
        entity.setDownloadLink(downloadLink);

        productRepository.save(entity);
    }

    private Product mapProductForCatalog(ProductEntity entity) {
        return Product.builder().id(entity.getId()).title(entity.getTitle()).description(entity.getDescription())
                .category(entity.getCategory() == null ? "Без категории" : entity.getCategory().getTitle())
                .developer(entity.getDeveloper()).imageLink(entity.getImageLink())
                .developerLink(entity.getDeveloperLink())
                .build();
    }

    private Product mapProduct(ProductEntity entity) {
        return Product.builder().id(entity.getId()).title(entity.getTitle()).description(entity.getDescription())
                .developer(entity.getDeveloper()).price(entity.getPrice()).downloadLink(entity.getDownloadLink())
                .category(entity.getCategory() == null ? "Без категории" : entity.getCategory().getTitle())
                .imageLink(entity.getImageLink()).developerLink(entity.getDeveloperLink())
                .preferences(entity.getPreferences())
                .build();
    }

    private Category mapCategory(CategoryEntity entity) {
        return Category.builder().id(entity.getId()).title(entity.getTitle()).build();
    }

}
