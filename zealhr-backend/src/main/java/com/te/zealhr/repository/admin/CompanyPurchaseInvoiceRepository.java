package com.te.zealhr.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.account.CompanyPurchaseInvoice;

public interface CompanyPurchaseInvoiceRepository extends JpaRepository<CompanyPurchaseInvoice, Long> {


	List<CompanyPurchaseInvoice> findByPurchaseInvoiceIdAndCompanyPurchaseOrderPurchaseOrderId(Long purchaseInvoiceId,
			Long purchaseOrderId);

	List<CompanyPurchaseInvoice> findByCompanyPurchaseOrderPurchaseOrderId(Long purchaseOrderId);

	List<CompanyPurchaseInvoice> findByCompanyInfoCompanyId(Long companyId);


	List<CompanyPurchaseInvoice> findByCompanyInfoCompanyIdAndPurchaseInvoiceId(Long companyId, Long purchaseInvoiceId);

	
}
