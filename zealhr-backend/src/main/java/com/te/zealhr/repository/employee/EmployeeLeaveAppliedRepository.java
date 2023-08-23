package com.te.zealhr.repository.employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.employee.EmployeeLeaveApplied;

public interface EmployeeLeaveAppliedRepository extends JpaRepository<EmployeeLeaveApplied, Long> {
	List<EmployeeLeaveApplied> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyId(String status, Long companyId);

	List<EmployeeLeaveApplied> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(
			String status, Long companyId, List<Long> employeeInfoIdList);

	Optional<EmployeeLeaveApplied> findByLeaveAppliedIdAndEmployeePersonalInfoCompanyInfoCompanyIdAndStatus(
			Long leaveAppliedId, Long companyId, String status);

	Optional<List<EmployeeLeaveApplied>> findByEmployeePersonalInfoEmployeeInfoIdAndStatusIgnoreCase(Long employeeInfoId,
			String status);

	Optional<EmployeeLeaveApplied> findByLeaveAppliedIdAndEmployeePersonalInfoCompanyInfoCompanyId(Long leaveAppliedId,
			Long companyId);

	Optional<List<EmployeeLeaveApplied>> findByLeaveOfTypeAndStatusInAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
			String leaveOfType, List<String> status, Long employeeInfoId, Long companyId);

	Long countByStatusAndEmployeePersonalInfoEmployeeInfoId(String status, Long employeeInfoId);

	Long countByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);

	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
	
	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoIdOrderByStartDateDesc(Long employeeInfoId);
	
	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoIdAndStartDateBetweenOrderByStartDateDesc(Long employeeInfoId, LocalDate startDate, LocalDate endDate);
	
	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoIdAndStartDateBetween(Long employeeInfoId, LocalDate startDate, LocalDate endDate);

	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoIdAndStatusOrderByStartDateDesc(
			Long employeeInfoId, String status);
	
	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoIdAndStatusAndStartDateBetweenOrderByStartDateDesc(
			Long employeeInfoId, String status, LocalDate startDate, LocalDate endDate);
	
	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoIdAndStatus(
			Long employeeInfoId, String status);

	Optional<EmployeeLeaveApplied> findByLeaveAppliedIdAndEmployeePersonalInfoEmployeeInfoId(Long leaveAppliedId,
			Long employeeInfoId);

	List<EmployeeLeaveApplied> findByStatusIgnoreCaseAndEmployeePersonalInfoEmployeeInfoIdAndStartDateBetween(
			String status, Long employeeInfoId, LocalDate startDate, LocalDate endDate);
	
	List<EmployeeLeaveApplied> findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdInAndStartDateBetween(
			Long companyId, List<Long> employeeInfoIdList, LocalDate startDate, LocalDate endDate);
}
