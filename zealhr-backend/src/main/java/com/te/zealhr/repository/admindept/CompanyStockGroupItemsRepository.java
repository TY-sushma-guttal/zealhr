package com.te.zealhr.repository.admindept;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.account.CompanyPurchaseOrder;
import com.te.zealhr.entity.account.CompanySalesOrder;
import com.te.zealhr.entity.admin.CompanyStockGroup;
import com.te.zealhr.entity.admindept.CompanyStockGroupItems;

public interface CompanyStockGroupItemsRepository extends JpaRepository<CompanyStockGroupItems, Long> {
	List<CompanyStockGroupItems> findByCompanyInfoCompanyId(Long companId);
	
	CompanyStockGroupItems findByCompanyInfoCompanyIdAndStockGroupItemId(Long companId,Long stockGroupItemId);
	
	
	List<CompanyStockGroupItems> findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndCompanyStockGroupAndInOut(String productName,CompanyPurchaseOrder companyPurchaseOrder,CompanySalesOrder companySalesOrder,CompanyStockGroup companyStockGroup,String inOut);
}
