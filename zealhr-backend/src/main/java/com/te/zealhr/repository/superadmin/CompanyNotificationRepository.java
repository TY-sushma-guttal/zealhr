package com.te.zealhr.repository.superadmin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.superadmin.CompanyNotification;

public interface CompanyNotificationRepository extends JpaRepository<CompanyNotification, Long> {

	List<CompanyNotification> findByCompanyInfoCompanyId(Long companyId);
	
	List<CompanyNotification> findByCompanyInfoCompanyIdAndIsSeenFalse(Long companyId);

	List<CompanyNotification> findByIsSeenTrue();
}
