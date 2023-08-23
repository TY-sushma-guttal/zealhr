package com.te.zealhr.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.employee.EmployeeEducationInfo;

@Repository
public interface EmployeeEducationInfoRepo extends JpaRepository<EmployeeEducationInfo, Long>{

}
