package com.te.zealhr.repository.admin;
/**
 * @author Tapas
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.admin.CompanyStockGroup;

public interface CompanyStockGroupRepository extends JpaRepository<CompanyStockGroup, Long> {
	
	void deleteByStockGroupId(Long stockGroupId);
	
	CompanyStockGroup findByStockGroupName(String stockGroupName);

	CompanyStockGroup findByStockGroupNameAndCompanyInfoCompanyId(String stockGroupName,Long comapanyId);



}
