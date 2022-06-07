package com.ifcdpp.ifcdpp.repo;

import com.ifcdpp.ifcdpp.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

}
