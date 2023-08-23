package com.te.zealhr.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.admin.CompanyDesignationInfo;

public interface CompanyDesignationInfoRepository extends JpaRepository<CompanyDesignationInfo, Long> {

	List<CompanyDesignationInfo> findByDesignationNameAndCompanyInfoCompanyId(String designationName, Long companyId);

	List<CompanyDesignationInfo> findByCompanyInfoCompanyId(Long companyId);

	Optional<CompanyDesignationInfo> findByDesignationName(String designation);

	List<CompanyDesignationInfo> findByDepartmentAndCompanyInfoCompanyId(String department, Long companyId);

	Optional<CompanyDesignationInfo> findByDesignationNameAndDepartmentAndCompanyInfoCompanyId(String designation,
			String department, Long companyId);

}
