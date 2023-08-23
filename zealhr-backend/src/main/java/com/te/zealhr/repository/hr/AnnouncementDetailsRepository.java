package com.te.zealhr.repository.hr;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.hr.AnnouncementDetails;

public interface AnnouncementDetailsRepository extends JpaRepository<AnnouncementDetails, Long> {

	List<AnnouncementDetails> findByCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(Long companyId,
			List<Long> employeeIds);

	List<AnnouncementDetails> findByCompanyInfoCompanyIdAndCreatedDateIsBetween(Long companyId, LocalDateTime after,
			LocalDateTime before);

}
