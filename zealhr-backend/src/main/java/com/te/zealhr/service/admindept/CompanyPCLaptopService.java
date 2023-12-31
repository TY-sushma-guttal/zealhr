package com.te.zealhr.service.admindept;

import java.util.List;

import com.te.zealhr.dto.admindept.CompanyPCLaptopDTO;
import com.te.zealhr.dto.admindept.ProductNameDTO;
import com.te.zealhr.dto.admindept.SubjectDTO;

/**
 * 
 * @author Brunda
 *
 */

public interface CompanyPCLaptopService {

	CompanyPCLaptopDTO createCompanyPCLaptop(Long companyId, CompanyPCLaptopDTO companyPCLaptopDTO);

	CompanyPCLaptopDTO updateCompanyPCLaptop(Long companyId, CompanyPCLaptopDTO companyPCLaptopDTO );  
	
	String updateCompanyPcLaptopEmployeeInfo(Long companyId, CompanyPCLaptopDTO companyPCLaptopDTO);
	
	List<CompanyPCLaptopDTO> companyPCLaptopDetails(Long companyId);

	List<SubjectDTO> getAllSubjects(Long companyId, String inOut,Integer status);

	List<ProductNameDTO> getProducts(Long companyId, String inOut, Long subjectId);

	CompanyPCLaptopDTO getcompanyPCLaptop(Long companyId, String serialNumber);

	List<String> getAllSerialNumber();

	String markNotWorking(Long companyId, CompanyPCLaptopDTO companyPCLaptopDTO);

}
