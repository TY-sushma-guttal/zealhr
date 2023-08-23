
package com.te.zealhr.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.employee.EmployeeLoginInfo;

/**
 * @author Sahid
 *
 */
public interface EmployeeLoginInfoRepository extends JpaRepository<EmployeeLoginInfo, Long> {

	Optional<List<EmployeeLoginInfo>> findByEmployeeId(String employeeId);

	Optional<EmployeeLoginInfo> findByEmployeeIdAndEmployeePersonalInfoCompanyInfoCompanyId(String employeeId,
			Long companyId);

	Optional<EmployeeLoginInfo> findByEmployeeIdAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoIsActiveTrue(
			String employeeId, Long companyId);
	
	Optional<EmployeeLoginInfo> findByEmailIdAndEmployeePersonalInfoIsActiveTrueAndEmployeePersonalInfoCompanyInfoIsActiveTrueAndEmployeePersonalInfoCompanyInfoCompanyCode(String emailId,
			String companyCode);

	Optional<EmployeeLoginInfo> findByEmployeeIdAndEmployeePersonalInfoIsActive(String employeeId, Boolean isActive);

	Optional<EmployeeLoginInfo> findByEmployeePersonalInfoEmployeeOfficialInfoOfficialEmailIdAndEmployeePersonalInfoIsActive(
			String emailId, Boolean isActive);
	
	EmployeeLoginInfo findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);

	EmployeeLoginInfo findByEmployeePersonalInfoEmployeeOfficialInfoOfficialEmailIdAndEmployeePersonalInfoIsActiveFalse(
			String emailId);

	Optional<EmployeeLoginInfo> findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeOfficialInfoOfficialId(
			Long companyId, Long officialId);
	
	EmployeeLoginInfo findByEmailIdAndEmployeePersonalInfoCompanyInfoCompanyCode(
			String emailId, String companyCode);
}
