package com.te.zealhr.service.it;

import java.util.List;

import com.te.zealhr.dto.admindept.CompanyHardwareItemsDTO;
import com.te.zealhr.dto.admindept.CompanyPCLaptopDTO;

public interface ITPcLaptopNonWorkingItemService {
	
	/* Not Working Laptop and PC details */
	public List<CompanyPCLaptopDTO> getNotWorkingPCLaptopDetails(Long companyId);

	public CompanyPCLaptopDTO getNotWorkingPCLaptopDetailsAndHistory(Long companyId, String serialNumber);

	public List<CompanyHardwareItemsDTO> getNotWorkingOtherItems(Long companyId);

	public CompanyHardwareItemsDTO getNotWorkingOtherItemsDetailsAndHistory(Long companyId,
			String indentificationNumber);

}
