package com.te.zealhr.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.admin.CompanyPayrollInfo;
import com.te.zealhr.entity.employee.EmployeeAnnualSalary;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;

@Repository
public interface EmployeeAnnualSalaryRepository  extends JpaRepository<EmployeeAnnualSalary, Long>{
	
	List<EmployeeAnnualSalary> findByEmployeePersonalInfoCompanyInfoCompanyId(Long companyId);
	
	EmployeeAnnualSalary findByEmployeePersonalInfo(EmployeePersonalInfo employeePersonalInfo);

	List<EmployeeAnnualSalary> findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
	
	List<EmployeeAnnualSalary> findByCompanyPayrollInfoIn(List<CompanyPayrollInfo> companyPayrollInfoList);
	
}
