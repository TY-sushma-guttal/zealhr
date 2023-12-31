package com.te.zealhr.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.employee.EmployeeReferenceInfo;

@Repository
public interface EmployeeReferenceInfoRepository1 extends JpaRepository<EmployeeReferenceInfo,Long> {

	List<EmployeeReferenceInfo> findByEmployeePersonalInfoCompanyInfoCompanyId(Long companyId);
}
