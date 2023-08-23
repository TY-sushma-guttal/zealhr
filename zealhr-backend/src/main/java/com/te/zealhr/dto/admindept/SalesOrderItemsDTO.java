package com.te.zealhr.dto.admindept;

import java.io.Serializable;

import com.te.zealhr.entity.account.CompanySalesOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalesOrderItemsDTO implements Serializable {

	private Long saleItemId;

	private String productName;

	private CompanySalesOrder companySalesOrder;

}
