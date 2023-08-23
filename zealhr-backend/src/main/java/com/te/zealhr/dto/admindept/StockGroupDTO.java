package com.te.zealhr.dto.admindept;

/**
 * @author Tapas
 *
 */

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockGroupDTO {
	private String stockGroupName;	//CompanyStockGroup
	private Long stockGroupId;		//CompanyStockGroup
}
