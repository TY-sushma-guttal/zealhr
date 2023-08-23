package com.te.zealhr.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.base.Optional;
import com.te.zealhr.entity.employee.EmployeeReviseSalary;

public interface EmployeeReviseSalaryRepository extends JpaRepository<EmployeeReviseSalary, Long>{

	List<EmployeeReviseSalary> findByCompanyInfoCompanyIdAndAmountNotNullAndRevisedDateNullAndReasonNotNull(Long companyId);
	
	List<EmployeeReviseSalary> findByCompanyInfoCompanyIdAndRevisedDateNull(Long companyId);

	List<EmployeeReviseSalary> findByCompanyInfoCompanyIdAndReviseSalaryId(Long companyId, Long reviseSalaryId);

	List<EmployeeReviseSalary> findByCompanyInfoCompanyId(Long companyId);
	
	List<EmployeeReviseSalary> findByEmployeePersonalInfoEmployeeInfoIdIn(List<Long> collect);

}
