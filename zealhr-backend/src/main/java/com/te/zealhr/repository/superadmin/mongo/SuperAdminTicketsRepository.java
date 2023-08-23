package com.te.zealhr.repository.superadmin.mongo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.helpandsupport.mongo.SuperAdminTickets;

@Repository
public interface SuperAdminTicketsRepository extends MongoRepository<SuperAdminTickets, String> {

	List<SuperAdminTickets> findByCompanyId(Long companyId);

	Optional<SuperAdminTickets> findByCompanyIdAndId(Long companyId, String id);
	
	List<SuperAdminTickets> findByCategoryAndCompanyIdAndTicketHistroysDate(String category, Long companyId,
			LocalDate date);

}
