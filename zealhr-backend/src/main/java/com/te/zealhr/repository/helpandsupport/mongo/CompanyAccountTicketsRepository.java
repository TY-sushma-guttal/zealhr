package com.te.zealhr.repository.helpandsupport.mongo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.zealhr.entity.helpandsupport.mongo.CompanyAccountTickets;

public interface CompanyAccountTicketsRepository extends MongoRepository<CompanyAccountTickets, String> {

	List<CompanyAccountTickets> findByCategoryAndSubCategoryAndCompanyIdAndEmployeeIdAndTicketHistroysDateAndIdentificationNumber(
			String category, String subCategory, Long companyId, String employeeId, LocalDate date,
			String identificationNumber);

	Optional<List<CompanyAccountTickets>> findByCategoryAndCompanyIdAndIdentificationNumberIn(String category,
			Long companyId, List<String> identificationNumber);

	List<CompanyAccountTickets> findByCategoryAndSubCategoryAndCompanyIdAndEmployeeId(String category,
			String subCategory, Long companyId, String employeeId);

//	Long findByCompanyId(Long companyId);

	List<CompanyAccountTickets> findByCompanyId(Long companyId);

	List<CompanyAccountTickets> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	Optional<CompanyAccountTickets> findByCompanyIdAndObjectTicketId(Long companyId, String objectTicketId);

	public Optional<CompanyAccountTickets> findByCompanyIdAndObjectTicketIdAndTicketHistroysDepartment(Long companyId,
			String id, String department);

	public Optional<CompanyAccountTickets> findByCompanyIdAndTicketHistroysDepartment(Long companyId,
			String department);

	List<CompanyAccountTickets> findByCompanyIdAndEmployeeId(Long companyId, String employeeId);

	List<CompanyAccountTickets> findByCompanyIdAndTicketHistroysByAndTicketHistroysStatus(Long companyId, Long by,
			String status);

	List<CompanyAccountTickets> findByCompanyIdAndCreatedBy(Long companyId, Long employeeInfoId);

	List<CompanyAccountTickets> findByCompanyIdAndCreatedByAndCreatedDateBetween(Long companyId, Long employeeInfoId,
			LocalDateTime atStartOfDay, LocalDateTime atStartOfDay2);
	
	List<CompanyAccountTickets> findByCreatedByInAndTicketHistroysStatusNotIn(List<Long> employeeInfoIdList, String status);

}
