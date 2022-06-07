package com.ifcdpp.ifcdpp.repo;

import com.ifcdpp.ifcdpp.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findAllByTitleStartsWithIgnoreCase(String title, Pageable pageable);

    Page<ProductEntity> findAllByCategory_Id(Long categoryId, Pageable pageable);

    Page<ProductEntity> findAllByCategory_IdAndTitleStartsWithIgnoreCase(Long categoryId, String title, Pageable pageable);
}
