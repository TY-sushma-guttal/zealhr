package com.te.zealhr.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.zealhr.dto.admin.CompanyPayrollEarningDTO;
import com.te.zealhr.entity.admin.CompanyPayrollEarning;

public interface CompanyPayrollEarningRepository extends JpaRepository<CompanyPayrollEarning,Long>{

	@Query(value = "select new com.te.zealhr.dto.admin.CompanyPayrollEarningDTO(cpe.earningId,cpe.salaryComponent,cpe.type,cpe.value,cpe.isTaxable,cpe.companyPayrollInfo.payrollId)from CompanyPayrollEarning cpe where cpe.companyPayrollInfo.payrollId=?1")
	public Optional<List<CompanyPayrollEarningDTO>> findEarningByPayrollInfoId(Long payrollId);
}
