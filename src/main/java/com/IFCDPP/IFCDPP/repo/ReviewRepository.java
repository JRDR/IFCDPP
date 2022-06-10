package com.ifcdpp.ifcdpp.repo;

import com.ifcdpp.ifcdpp.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findAllByUser_Id(Long userId);
}
