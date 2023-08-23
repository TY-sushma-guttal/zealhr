package com.te.zealhr.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.employee.CompanyEmployeeResignationDetails;

public interface CompanyEmployeeResignationDetailsRepo extends JpaRepository<CompanyEmployeeResignationDetails, Long> {
	
	public List<CompanyEmployeeResignationDetails> findByCompanyInfoCompanyId(Long companyId);
	
}
