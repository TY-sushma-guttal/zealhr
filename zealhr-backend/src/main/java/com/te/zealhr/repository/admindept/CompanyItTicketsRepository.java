package com.te.zealhr.repository.admindept;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.zealhr.entity.helpandsupport.mongo.CompanyItTickets;

public interface CompanyItTicketsRepository extends MongoRepository<CompanyItTickets, String> {

	public List<CompanyItTickets> findByCompanyIdAndIdentificationNumber(Long companyId, String identificationNumber);

	public List<CompanyItTickets> findByCompanyId(Long companyId);

	public List<CompanyItTickets> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	public List<CompanyItTickets> findByIdentificationNumber(String serialNumber);

	List<CompanyItTickets> findByCategoryAndSubCategoryAndCompanyIdAndEmployeeIdAndTicketHistroysDate(String category,
			String subCategory, Long companyId, String employeeId, LocalDate date);

	Optional<CompanyItTickets> findByCompanyIdAndId(Long companyId, String id);

	public Optional<CompanyItTickets> findByCompanyIdAndIdAndTicketHistroysDepartment(Long companyId, String id,
			String department);

	List<CompanyItTickets> findByCompanyIdAndEmployeeId(Long companyId, String employeeId);

	List<CompanyItTickets> findByCompanyIdAndTicketHistroysByAndTicketHistroysStatus(Long companyId, Long by,
			String status);

	public List<CompanyItTickets> findByCompanyIdAndCreatedBy(Long companyId, Long employeeInfoId);

	public List<CompanyItTickets> findByCompanyIdAndCreatedByAndCreatedDateBetween(Long companyId, Long employeeInfoId,
			LocalDateTime atStartOfDay, LocalDateTime atStartOfDay2);
	
	List<CompanyItTickets> findByCreatedByInAndTicketHistroysStatusNotIn(List<Long> employeeInfoIdList, String status);
}
