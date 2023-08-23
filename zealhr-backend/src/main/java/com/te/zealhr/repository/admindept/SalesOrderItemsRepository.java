package com.te.zealhr.repository.admindept;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.account.SalesOrderItems;

/**
 * 
 * @author Brunda
 *
 */
@Repository
public interface SalesOrderItemsRepository extends JpaRepository<SalesOrderItems, Long> {

//	SalesOrderItems findByProductname(String productName);

	SalesOrderItems findByProductNameAndCompanySalesOrderSalesOrderId(String productName, Long salesOrderId);

	List<SalesOrderItems> findByCompanySalesOrderSalesOrderId(Long salesOrderId);

	// findBycompanySalesOrder

}
