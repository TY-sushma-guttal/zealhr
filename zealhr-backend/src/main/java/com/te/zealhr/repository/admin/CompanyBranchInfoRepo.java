package com.te.zealhr.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.admin.CompanyBranchInfo;

public interface CompanyBranchInfoRepo extends JpaRepository<CompanyBranchInfo, Long>{
	
	public Optional<CompanyBranchInfo> findByBranchName(String branchName);
	
}
