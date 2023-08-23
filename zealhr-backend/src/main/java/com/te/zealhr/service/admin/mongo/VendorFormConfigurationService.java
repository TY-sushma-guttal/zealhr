package com.te.zealhr.service.admin.mongo;

import com.te.zealhr.dto.admin.mongo.VendorFormConfigurationDto;

public interface VendorFormConfigurationService {
	String addVendorFormConfiguration(VendorFormConfigurationDto vendorFormConfigurationDto, Long companyId);

	VendorFormConfigurationDto getVendorFormConfiguration(Long companyId);
}
