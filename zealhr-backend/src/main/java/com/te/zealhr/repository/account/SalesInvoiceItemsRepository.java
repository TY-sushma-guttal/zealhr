package com.te.zealhr.repository.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.account.SalesInvoiceItems;

public interface SalesInvoiceItemsRepository extends JpaRepository<SalesInvoiceItems, Long> {
	
	List<SalesInvoiceItems> findBySalesOrderItemsSaleItemId(Long saleItemId);

}
