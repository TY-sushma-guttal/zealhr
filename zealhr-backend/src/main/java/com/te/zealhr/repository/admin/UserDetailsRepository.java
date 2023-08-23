package com.te.zealhr.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.employee.EmployeePersonalInfo;

public interface UserDetailsRepository extends JpaRepository<EmployeePersonalInfo, Long> {

	
	
	

}
