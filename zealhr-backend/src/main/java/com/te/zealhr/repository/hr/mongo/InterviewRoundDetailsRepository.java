package com.te.zealhr.repository.hr.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.hr.mongo.InterviewRoundDetails;

/**
 * 
 * @author Ravindra
 *
 */
@Repository
public interface InterviewRoundDetailsRepository extends MongoRepository<InterviewRoundDetails, String> {

	List<InterviewRoundDetails> findByCompanyId(Long companyId);

}
