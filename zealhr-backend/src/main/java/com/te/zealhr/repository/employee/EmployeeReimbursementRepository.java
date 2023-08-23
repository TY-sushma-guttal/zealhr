package com.te.zealhr.repository.employee;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.te.zealhr.entity.employee.EmployeeReimbursementInfo;

@Repository
public interface EmployeeReimbursementRepository extends JpaRepository<EmployeeReimbursementInfo, Long> {

	EmployeeReimbursementInfo  findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyId(Long reimbursementId,Long companyId);

	List<EmployeeReimbursementInfo> findByEmployeePersonalInfoEmployeeInfoIdAndExpenseDateBetween(Long employeeInfoId,
			LocalDate startDate, LocalDate endDate);
	
	
}
