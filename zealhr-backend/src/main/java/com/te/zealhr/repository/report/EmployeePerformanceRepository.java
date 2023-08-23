package com.te.zealhr.repository.report;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.zealhr.entity.report.mongo.EmployeePerformance;

public interface EmployeePerformanceRepository extends MongoRepository<EmployeePerformance, String> {
	Optional<EmployeePerformance> findByCompanyIdAndEmployeeInfoIdAndYear(Long companyId, Long employeeInfoId,
			Long year);

	List<EmployeePerformance> findByCompanyIdAndYear(Long companyId, Long year);
	
	List<EmployeePerformance> findByCompanyIdAndEmployeeInfoIdAndYearIn(Long companyId, Long employeeInfoId,
			List<Long> year);
}
