package com.te.zealhr.repository.employee;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.employee.EmployeeAdvanceSalary;

@Repository
public interface EmployeeAdvanceSalaryRepository extends JpaRepository<EmployeeAdvanceSalary, Long> {

	Optional<EmployeeAdvanceSalary> findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(
			Long advanceSalaryId, Long companyId);

	Optional<EmployeeAdvanceSalary> findByAdvanceSalaryIdAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
			Long advanceSalaryId, Long employeeInfoId, Long companyId);

	List<EmployeeAdvanceSalary> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyId(String status, Long companyId);

	List<EmployeeAdvanceSalary> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(
			String status, Long companyId, List<Long> employeeInfoIdList);

	List<EmployeeAdvanceSalary> findByEmployeePersonalInfoCompanyInfoCompanyId(Long companyId);

	List<EmployeeAdvanceSalary> findByEmployeePersonalInfoCompanyInfoCompanyIdAndStatusIgnoreCaseAndIsPaidIsNullOrIsPaid(
			Long companyId, String status, Boolean isPaid);

	List<EmployeeAdvanceSalary> findByAdvanceSalaryIdInAndEmployeePersonalInfoCompanyInfoCompanyId(
			ArrayList<Long> employeeAdvanceSalaryList, Long companyId);

	List<EmployeeAdvanceSalary> findByEmployeePersonalInfoEmployeeInfoIdAndCreatedDateBetween(Long employeeInfoId,
			LocalDateTime startDate, LocalDateTime endDate);
	
	List<EmployeeAdvanceSalary> findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdInAndCreatedDateBetween(
			Long companyId, List<Long> employeeInfoIdList,LocalDateTime startDate, LocalDateTime endDate);

}
