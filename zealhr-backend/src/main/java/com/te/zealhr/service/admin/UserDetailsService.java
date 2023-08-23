package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.AddExistingEmployeeDataRequestDto;
import com.te.zealhr.dto.admin.BranchInfoDto;
import com.te.zealhr.dto.admin.DepartmentInfoDto;
import com.te.zealhr.dto.admin.DesignationInfoDto;
import com.te.zealhr.dto.admin.EmployeeDataDto;
import com.te.zealhr.dto.admin.EmployeeOfficialInfoDTO;
import com.te.zealhr.dto.admin.EmployeeUploadDTO;
import com.te.zealhr.dto.admin.WorkWeekInfoDto;
import com.te.zealhr.dto.admindept.ProductNameDTO;
import com.te.zealhr.dto.employee.EmployeeIdDto;
/**
 * @author Tapas
 *
 */

public interface UserDetailsService {

	/*
	 * method to find all the employee
	 */
	public List<EmployeeDataDto> userDetails(long companyId);

	/*
	 * API for User Management Details (Find by ID)
	 */
	public EmployeeOfficialInfoDTO userManagementDetails(Long companyId, Long officialId);

	/*
	 * API for status active/inactive
	 * */
	public String updateStatus(Long companyId, Long officialId,String employeeId, ProductNameDTO employeeStatusUpdateDTO);
	
	public String updateUserDetails(Long companyId, Long officialId,String employeeId,EmployeeOfficialInfoDTO employeeDataDto); 
	//get all data on add existing employee
	

	
	public List<BranchInfoDto> getAllBranchInfo(Long companyId);
	
	public List<DepartmentInfoDto> getAllDepartmentInfo(Long companyId);
	
	public List<DesignationInfoDto> getAllDesignationInfo(Long companyId,String department);
	
	public List<WorkWeekInfoDto> getAllWorkInfo(Long companyId);
	
	public List<EmployeeIdDto> getAllEmployeeName(Long companyId);
	
	//add all details of existing employee
	
	public String addExistingEmployee(Long companyId,String employeeId, AddExistingEmployeeDataRequestDto addExistingEmployeeDataRequestDto);
	
	String employeeBlukUpload(List<EmployeeUploadDTO> employeeUploadDTOList, Long companyId, Long userId);

	

}
