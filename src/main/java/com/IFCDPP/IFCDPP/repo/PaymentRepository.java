package com.ifcdpp.ifcdpp.repo;

import com.ifcdpp.ifcdpp.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByProduct_IdAndUser_Id(Long productId, Long userId);
}
