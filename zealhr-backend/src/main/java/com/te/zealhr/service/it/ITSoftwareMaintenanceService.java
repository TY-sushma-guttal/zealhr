package com.te.zealhr.service.it;

import java.util.List;

import com.te.zealhr.dto.admindept.CompanyPCLaptopDTO;
import com.te.zealhr.dto.admindept.PcLaptopSoftwareDetailsDTO;
import com.te.zealhr.dto.it.mongo.PcLaptopSoftwareRenewalDTO;

public interface ITSoftwareMaintenanceService {

	public List<CompanyPCLaptopDTO> getITSoftwareMaintenanceDetails(Long companyId);

	public List<PcLaptopSoftwareDetailsDTO> getITSoftwareMaintenanceDetailsList(Long companyId, String serialNumber);

	public PcLaptopSoftwareDetailsDTO createOrUpdateNewSoftwares(PcLaptopSoftwareDetailsDTO laptopSoftwareDetailsDTO,
			Long companyId, Long employeeInfoId,String serialNumber);

	public PcLaptopSoftwareRenewalDTO updateRenewalStatus(PcLaptopSoftwareRenewalDTO pcLaptopSoftwareRenewalDTO,
			Long companyId, Long employeeInfoId);
}
