package com.te.zealhr.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.zealhr.dto.admin.CompanyPayrollDeductionDTO;
import com.te.zealhr.entity.admin.CompanyPayrollDeduction;

public interface CompanyPayrollDeductionRepository extends JpaRepository<CompanyPayrollDeduction,Long> {

	
	@Query(value = "select new com.te.zealhr.dto.admin.CompanyPayrollDeductionDTO(cpd.deductionId,cpd.title,cpd.type,cpd.value,cpd.companyPayrollInfo.payrollId)from CompanyPayrollDeduction cpd where cpd.companyPayrollInfo.payrollId=?1")
	public Optional<List<CompanyPayrollDeductionDTO>> findDeductionByPayrollInfoId(Long payrollId);
}
