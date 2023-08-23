package com.te.zealhr.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.employee.EmployeeLoginInfo;

public interface EmployeeLoginRepository extends JpaRepository<EmployeeLoginInfo, Long>{

}
