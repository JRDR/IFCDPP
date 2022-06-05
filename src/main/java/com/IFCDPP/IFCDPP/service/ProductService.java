package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.CategoryEntity;
import com.ifcdpp.ifcdpp.entity.ProductEntity;
import com.ifcdpp.ifcdpp.models.Catalog;
import com.ifcdpp.ifcdpp.models.Category;
import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.repo.CategoryRepository;
import com.ifcdpp.ifcdpp.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Catalog getCatalogOnPage(Integer page, Long categoryId) {
        if (page == null) {
            page = 1;
        }

        Pageable pageWithFiveElems = PageRequest.of(page - 1, 5);
        Page<ProductEntity> entities;
        if (categoryId == null) {
            entities = productRepository.findAll(pageWithFiveElems);
        } else {
            entities = productRepository.findAllByCategory_Id(categoryId, pageWithFiveElems);
        }
        List<Product> products = entities.stream().map(this::mapProductForCatalog).collect(Collectors.toList());
        int pagesNum = entities.getTotalPages();

        return new Catalog(products, pagesNum);
    }

    public Product getProductById(Long id) {
        Optional<ProductEntity> entity = productRepository.findById(id);
        return entity.map(this::mapProduct).orElse(null);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mapCategory).collect(Collectors.toList());
    }

    public void saveProduct(Product product) {
        ProductEntity entity = new ProductEntity();

        entity.setTitle(product.getTitle());
        entity.setDescription(product.getDescription());
        entity.setDeveloper(product.getDeveloper());
        entity.setPrice(product.getPrice());
        entity.setDownloadLink(product.getDownloadLink());
        entity.setImageLink(product.getImageLink());

        Optional<CategoryEntity> category = categoryRepository.findByTitle(product.getCategory());
        category.ifPresent(entity::setCategory);

        productRepository.save(entity);

    }

    private Product mapProductForCatalog(ProductEntity entity) {
        return Product.builder().id(entity.getId()).title(entity.getTitle()).description(entity.getDescription())
                .category(entity.getCategory() == null ? "Без категории" : entity.getCategory().getTitle())
                .imageLink(entity.getImageLink())
                .build();
    }

    private Product mapProduct(ProductEntity entity) {
        return Product.builder().id(entity.getId()).title(entity.getTitle()).description(entity.getDescription())
                .developer(entity.getDeveloper()).price(entity.getPrice()).downloadLink(entity.getDownloadLink())
                .category(entity.getCategory() == null ? "Без категории" : entity.getCategory().getTitle())
                .imageLink(entity.getImageLink())
                .build();
    }

    private Category mapCategory(CategoryEntity entity) {
        return Category.builder().id(entity.getId()).title(entity.getTitle()).build();
    }

}
