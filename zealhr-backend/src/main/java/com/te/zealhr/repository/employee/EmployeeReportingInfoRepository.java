package com.te.zealhr.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.employee.EmployeeReportingInfo;

@Repository
public interface EmployeeReportingInfoRepository extends JpaRepository<EmployeeReportingInfo,Long>{
	
	List<EmployeeReportingInfo> findByReportingHREmployeeInfoId(Long employeeInfoId);
	
	List<EmployeeReportingInfo> findByReportingManagerEmployeeInfoId(Long employeeInfoId);
	
	Optional<List<EmployeeReportingInfo>> findByReportingManagerEmployeeInfoIdAndEmployeePersonalInfoNotNull(Long employeeInfoId);
	
	List<EmployeeReportingInfo> findByReportingManagerEmployeeInfoIdAndReportingManagerCompanyInfoCompanyId(Long employeeInfoId, Long companyId);
	
	EmployeeReportingInfo findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
	
	//List<EmployeeReportingInfo> findByReportingHREmployeeInfoIdAndEmployeeInfoIdReportingHRCompanyInfoCompanyId(Long employeeInfoId, Long companyId);

}
