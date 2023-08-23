package com.te.zealhr.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.admin.CompanyDepartmentDetails;

@Repository
public interface CompanyDepartmentDetailsRepository extends JpaRepository<CompanyDepartmentDetails, Long>{
	
	Optional<CompanyDepartmentDetails> findByCompanyDepartmentNameAndCompanyInfoCompanyId(String companyDepartmentName, Long companyId);
	
	Optional<List<CompanyDepartmentDetails>> findByCompanyDepartmentNameIgnoreCaseInAndCompanyInfoCompanyId(List<String> companyDepartmentNameList, Long companyId);
	
	List<CompanyDepartmentDetails> findByCompanyInfoCompanyId(Long companyId);
	
	Optional<List<CompanyDepartmentDetails>> findByCompanyInfoCompanyIdAndParentDepartmentInfoCompanyDepartmentId(Long companyId, Long parentCompanyDepartmentId);

}
