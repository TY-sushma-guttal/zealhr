package com.te.zealhr.repository.employee.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.zealhr.entity.employee.mongo.EmployeeDocumentDetails;

public interface EmployeeDocumentDetailsRepository extends MongoRepository<EmployeeDocumentDetails, String>{
	
	List<EmployeeDocumentDetails> findByEmployeeIdAndCompanyId(String employeeId, Long companyId);

}
