package com.te.zealhr.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.account.PurchaseInvoiceItems;

public interface PurchaseInvoiceItemsRepository extends JpaRepository<PurchaseInvoiceItems, Long>{

	List<PurchaseInvoiceItems> findByCompanyPurchaseInvoicePurchaseInvoiceId(Long purchaseInvoiceId);

	List<PurchaseInvoiceItems> findByPurchaseOrderItemsCompanyPurchaseOrderPurchaseOrderId(Long purchaseOrderId);


}
