package com.backend.api.repository;

import com.backend.api.entity.AssignDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignDistributionRepository extends JpaRepository<AssignDistribution,Long> {
}
