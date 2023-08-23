package com.te.zealhr.service.account;

import java.util.ArrayList;
import java.util.List;

import com.te.zealhr.dto.account.AccountClientDetailsDTO;
import com.te.zealhr.dto.account.SendVendorLinkDTO;
import com.te.zealhr.dto.account.VendorBasicDetailsDTO;
import com.te.zealhr.dto.account.VendorContactDetailsDTO;
import com.te.zealhr.dto.account.VendorDetailsDTO;
import com.te.zealhr.dto.account.VendorFormDTO;
import com.te.zealhr.dto.account.VendorListDTO;

public interface VendorManagementService {
	
	List<VendorFormDTO> getDynamicFactors(Long companyId);
	
	VendorDetailsDTO saveVendorDetails(VendorDetailsDTO vendorDetailsDTO);
	
	List<VendorBasicDetailsDTO> getVendorBasicDetails(Long companyId);
	
	VendorDetailsDTO getVendorDetailsById(String vendorId);
	
	String sendLink(SendVendorLinkDTO sendVendorLinkDTO, Long userId);
	
	ArrayList<VendorListDTO> vendorList(Long companyId);

	VendorContactDetailsDTO contactDetails(Long companyId, String id);

	ArrayList<AccountClientDetailsDTO> clientDetails(Long companyId);
	
	VendorDetailsDTO updatePaymentDetails(VendorDetailsDTO vendorDetailsDTO);

}
