package com.backend.api.repository;

import com.backend.api.entity.DistributionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionRecordRepository extends JpaRepository<DistributionRecord,Long> {
}
