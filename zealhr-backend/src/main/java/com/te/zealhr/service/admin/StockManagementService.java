package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.CompanyStockCategoriesDTO;
import com.te.zealhr.dto.admin.CompanyStockGroupDTO;
import com.te.zealhr.dto.admin.CompanyStockUnitsDTO;
import com.te.zealhr.dto.admin.StockCategoriesDTO;
import com.te.zealhr.dto.admin.StockGroupDTO;
import com.te.zealhr.dto.admin.StockUnitDto;

/**
 * @author Tapas
 *
 */
public interface StockManagementService {

	public List<CompanyStockGroupDTO> getStockGroup(Long companyId);

	public StockGroupDTO saveStockGroup(Long companyId, StockGroupDTO stockGroupDTO);

	public List<CompanyStockUnitsDTO> getStockUnits(Long companyId);

	public StockUnitDto saveStockUnits(Long companyId, StockUnitDto stockUnitDto);


	public List<CompanyStockCategoriesDTO> getListStockCategory(Long companyId);
	
	public StockCategoriesDTO saveStockCategory(Long companyId, StockCategoriesDTO stockCategoriesDTO);
	
	

}
