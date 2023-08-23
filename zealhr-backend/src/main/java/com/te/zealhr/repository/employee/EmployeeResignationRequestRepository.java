package com.te.zealhr.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.employee.CompanyEmployeeResignationDetails;

@Repository
public interface EmployeeResignationRequestRepository extends JpaRepository<CompanyEmployeeResignationDetails, Long> {

	List<CompanyEmployeeResignationDetails> findByEmployeePersonalInfoEmployeeInfoIdAndCompanyInfoCompanyId(Long employeeInfoId,Long companyId);

}
