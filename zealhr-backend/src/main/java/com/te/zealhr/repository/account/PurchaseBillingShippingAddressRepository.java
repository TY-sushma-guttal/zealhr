package com.te.zealhr.repository.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.account.PurchaseBillingShippingAddress;

public interface PurchaseBillingShippingAddressRepository extends JpaRepository<PurchaseBillingShippingAddress, Long> {

	List<PurchaseBillingShippingAddress> findByCompanyPurchaseOrderPurchaseOrderId(Long purchaseOrderId);

	void deleteByCompanyPurchaseOrderPurchaseOrderId(Long purchaseOrderId);

}
