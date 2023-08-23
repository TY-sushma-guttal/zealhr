package com.te.zealhr.repository.employee.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.employee.mongo.EmployeeLetterDetails;

@Repository
public interface EmployeeLetterDetailsRepository extends MongoRepository<EmployeeLetterDetails, String> {
	
	List<EmployeeLetterDetails> findByEmployeeInfoIdAndCompanyId(Long employeeInfoId, Long companyId);
	
	List<EmployeeLetterDetails> findByCompanyIdAndLettersIsApproved(Long companyId, Boolean isApproved);

}
