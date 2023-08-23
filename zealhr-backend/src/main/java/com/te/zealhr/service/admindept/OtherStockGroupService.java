package com.te.zealhr.service.admindept;

import java.util.List;

import com.te.zealhr.dto.admindept.CreateOtherStockGroupItemDTO;
import com.te.zealhr.dto.admindept.EditOtherStockGroupItemDto;
import com.te.zealhr.dto.admindept.GetOtherStockGroupItemDto;
import com.te.zealhr.dto.admindept.ProductNameDTO;
import com.te.zealhr.dto.admindept.StockGroupDTO;
import com.te.zealhr.dto.admindept.SubjectDTO;



/**
 * @author Tapas
 *
 */

public interface OtherStockGroupService {
	public String createOtherStockGroupItem(CreateOtherStockGroupItemDTO createOtherStockGroupItemDTO,Long companyId);
	
	public List<GetOtherStockGroupItemDto> getAllOtherStockGroupItem(Long companyId);
	
	public GetOtherStockGroupItemDto getOtherStockGroupItem(Long stockGroupItemId);

	public String editOtherStockGroupItem(EditOtherStockGroupItemDto stockGroupItemDto,Long companyId);
	
	public List<StockGroupDTO> getAllStockGroup(Long companyId);
	
	public List<SubjectDTO> getAllSubject(Long stcokGroupId,String inOut);
	
	public List<ProductNameDTO> getAllProductName(Long companyId,Long subjectId,String inOut);
	
}
