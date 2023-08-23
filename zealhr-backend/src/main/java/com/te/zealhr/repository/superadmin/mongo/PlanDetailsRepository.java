package com.te.zealhr.repository.superadmin.mongo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.superadmin.PlanDetails;

@Repository
public interface PlanDetailsRepository extends JpaRepository<PlanDetails, Long> {
	Optional<PlanDetails> findByPlanName(String planName);
}
