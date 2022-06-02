package com.IFCDPP.IFCDPP.repo;

import com.IFCDPP.IFCDPP.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
