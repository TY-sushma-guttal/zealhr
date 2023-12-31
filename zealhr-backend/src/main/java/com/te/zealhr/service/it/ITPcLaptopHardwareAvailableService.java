package com.te.zealhr.service.it;

import java.util.List;

import com.te.zealhr.dto.admindept.CompanyHardwareItemsDTO;
import com.te.zealhr.dto.admindept.CompanyPCLaptopDTO;

public interface ITPcLaptopHardwareAvailableService {

	/* Working and available Laptop and PC details */
	public List<CompanyPCLaptopDTO> getITPCLaptopDetails(Long companyId);

	public CompanyPCLaptopDTO getITPCLaptopDetailsAndHistory(Long companyId, String serialNumber);

	public List<CompanyHardwareItemsDTO> getOtherItems(Long companyId);

	public CompanyHardwareItemsDTO getOtherItemsDetailsAndHistory(Long companyId, String indentificationNumber);

	

}
