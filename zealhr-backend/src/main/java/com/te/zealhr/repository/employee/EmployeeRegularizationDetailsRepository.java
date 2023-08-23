package com.te.zealhr.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.te.zealhr.entity.employee.EmployeeRegularizationDetails;

public interface EmployeeRegularizationDetailsRepository extends JpaRepository<EmployeeRegularizationDetails, Long> {

	List<EmployeeRegularizationDetails> findByEmployeePersonalInfoEmployeeInfoIdAndPunchInIsNotNullAndPunchOutIsNotNull(
			Long userId);

	List<EmployeeRegularizationDetails> findByEmployeePersonalInfoEmployeeInfoId(Long userId);

	List<EmployeeRegularizationDetails> findByEmployeePersonalInfoEmployeeInfoIdIn(List<Long> employeeInfoIdList);

}
