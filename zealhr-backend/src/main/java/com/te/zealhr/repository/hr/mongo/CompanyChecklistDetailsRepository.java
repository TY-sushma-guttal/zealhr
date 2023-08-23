package com.te.zealhr.repository.hr.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.hr.mongo.CompanyChecklistDetails;

/**
 * 
 * @author Ravindra
 *
 */
@Repository
public interface CompanyChecklistDetailsRepository extends MongoRepository<CompanyChecklistDetails, String> {

	List<CompanyChecklistDetails> findByCompanyId(Long companyId);

}
