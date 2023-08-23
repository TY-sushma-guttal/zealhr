package com.te.zealhr.service.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.te.zealhr.beancopy.BeanCopy;
import com.te.zealhr.constants.admin.AdminConstants;
import com.te.zealhr.dto.admin.CompanyDesignationInfoDto;
import com.te.zealhr.dto.admin.DeleteCompanyDesignationDto;
import com.te.zealhr.dto.admin.DesignationUploadDTO;
import com.te.zealhr.dto.admin.RoleDTO;
import com.te.zealhr.dto.employee.EmployeeCapabilityDTO;
import com.te.zealhr.entity.admin.CompanyDepartmentDetails;
import com.te.zealhr.entity.admin.CompanyDesignationInfo;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.exception.CompanyNotFoundException;
import com.te.zealhr.exception.DuplicateDesignationException;
import com.te.zealhr.exception.admin.DesignationCannotUpdate;
import com.te.zealhr.exception.admin.DesignationIdNotFoundException;
import com.te.zealhr.exception.employee.DataNotFoundException;
import com.te.zealhr.repository.admin.CompanyDepartmentDetailsRepository;
import com.te.zealhr.repository.admin.CompanyDesignationRepository;
import com.te.zealhr.repository.admin.CompanyInfoRepository;

@Service
public class CompanyDesignationServiceImpl implements CompanyDesignationService {

	private static final String DESIGNATION_CANNOT_BE_DELETED = "Designation Cannot Be Deleted";

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private CompanyDesignationRepository companyDesignationRepository;

	@Autowired
	private CompanyDepartmentDetailsRepository companyDepartmentDetailsRepository;

	@Override
	public CompanyDesignationInfoDto addCompanyDesignation(long companyId, long parentDesignationId,
			CompanyDesignationInfoDto companyDesignationInfoDto) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.TRUE.equals(companyInfo.getIsSubmited())) {
			throw new DesignationCannotUpdate("Designation Cannot Be Updated");
		}

		if (companyDesignationRepository
				.findByDepartmentAndDesignationNameAndCompanyInfoCompanyId(companyDesignationInfoDto.getDepartment(),
						companyDesignationInfoDto.getDesignationName(), companyId)
				.isPresent()) {
			throw new DuplicateDesignationException("Designation Name Already Exist!!");
		}
		CompanyDesignationInfo companyDesignationInfo = new CompanyDesignationInfo();
		BeanUtils.copyProperties(companyDesignationInfoDto, companyDesignationInfo);
		companyDesignationInfo.setCompanyInfo(companyInfo);

		CompanyDesignationInfo designationInfo = companyDesignationRepository.findById(parentDesignationId)
				.orElse(null);

		if (designationInfo != null) {
			companyDesignationInfo.setParentDesignationInfo(designationInfo);
		} else {
			companyDesignationInfo.setParentDesignationInfo(companyDesignationInfo);
		}

		companyDesignationRepository.save(companyDesignationInfo);
		BeanUtils.copyProperties(companyDesignationInfo, companyDesignationInfoDto);

		return companyDesignationInfoDto;
	}
	
	@Override
	@Transactional
	public String uploadCompanyDesignation(Long companyId, List<DesignationUploadDTO> designationUploadDTOList) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		List<CompanyDepartmentDetails> companyDepartmentDetailsList = companyInfo.getCompanyDepartmentDetailsList();

		if (companyDepartmentDetailsList.isEmpty()) {
			throw new DesignationCannotUpdate("Departments are Not Configured");
		}

		List<CompanyDesignationInfo> companyDesignationInfoList = companyInfo.getCompanyDesignationInfoList();

		List<String> departmentNotPresent = new ArrayList<>();

		List<String> parentDesignationNotPresent = new ArrayList<>();

		List<CompanyDesignationInfo> designationInfoList = new ArrayList<>();

		for (DesignationUploadDTO designationUploadDTO : designationUploadDTOList) {
			saveDesignation(companyDepartmentDetailsList, companyDesignationInfoList, departmentNotPresent,
					parentDesignationNotPresent, designationInfoList, designationUploadDTO, companyInfo);
		}

		companyDesignationRepository.saveAll(designationInfoList);

		String result = "";
		if (!departmentNotPresent.isEmpty()) {
			result = result + "Departments Not Present for : " + StringUtils.join(departmentNotPresent.toArray(), ',')
					+ ". ";
		}
		if (!parentDesignationNotPresent.isEmpty()) {
			result = result + "Parent Designations Not Present for : "
					+ StringUtils.join(parentDesignationNotPresent.toArray(), ',') + ". ";
		}
		return result.isEmpty() ? "Designations Uploaded Successfully" : result;
	}

	void saveDesignation(List<CompanyDepartmentDetails> companyDepartmentDetailsList,
			List<CompanyDesignationInfo> companyDesignationInfoList, List<String> departmentNotPresent,
			List<String> parentDesignationNotPresent, List<CompanyDesignationInfo> designationInfoList,
			DesignationUploadDTO designationUploadDTO, CompanyInfo companyInfo) {
		Optional<CompanyDepartmentDetails> departmentDetails = companyDepartmentDetailsList.stream()
				.filter(department -> department.getCompanyDepartmentName()
						.equalsIgnoreCase(designationUploadDTO.getDepartment()))
				.findAny();
		if (departmentDetails.isPresent()) {
			CompanyDesignationInfo companyDesignationInfo = new CompanyDesignationInfo();
			BeanUtils.copyProperties(designationUploadDTO, companyDesignationInfo);
			companyDesignationInfo.setCompanyInfo(companyInfo);
			companyDesignationInfo.setRoles(departmentDetails.get().getCompanyDepartmentRoles());
			if (designationUploadDTO.getParentDesignationName() != null) {
				Optional<CompanyDesignationInfo> parentDesignationOptional = companyDesignationInfoList.stream()
						.filter(designation -> designation.getDesignationName()
								.equalsIgnoreCase(designationUploadDTO.getParentDesignationName()))
						.findAny();
				if (parentDesignationOptional.isPresent()) {
					if (companyDesignationInfoList.stream().noneMatch(designation -> designation.getDesignationName()
							.equalsIgnoreCase(designationUploadDTO.getDesignationName()))) {
						companyDesignationInfo.setParentDesignationInfo(parentDesignationOptional.get());
						designationInfoList.add(companyDesignationInfo);
					}
				} else {
					parentDesignationNotPresent.add(designationUploadDTO.getDesignationName());
				}
			} else {
				companyDesignationInfo.setParentDesignationInfo(companyDesignationInfo);
				designationInfoList.add(companyDesignationInfo);
			}
		} else {
			departmentNotPresent.add(designationUploadDTO.getDesignationName());
		}
	}

	@Override
	public Object getRoleForDesinagtion(RoleDTO roleDTO) {
		CompanyDepartmentDetails department = companyDepartmentDetailsRepository.findById(roleDTO.getDepartmentId())
				.orElseThrow(() -> new DataNotFoundException("Department Not Found"));
		Object roles = department.getCompanyDepartmentRoles();
		List<EmployeeCapabilityDTO> employeeCapabilityDTOList = BeanCopy.objectProperties(roles,
				new TypeReference<List<EmployeeCapabilityDTO>>() {
				});
		if (roleDTO.getDesignationId() == null) {
			return employeeCapabilityDTOList.stream()
					.filter(capability -> capability.getIsEnable().equals(Boolean.TRUE)).map(capability -> {
						List<EmployeeCapabilityDTO> childCapabilityNameList = capability.getChildCapabilityNameList();
						if (childCapabilityNameList != null) {
							capability.setChildCapabilityNameList(childCapabilityNameList.stream()
									.filter(child -> child.getIsEnable().equals(Boolean.TRUE))
									.collect(Collectors.toList()));
						}
						return capability;
					}).collect(Collectors.toList());
		} else {
			CompanyDesignationInfo desination = companyDesignationRepository.findById(roleDTO.getDesignationId())
					.orElseThrow(() -> new DataNotFoundException("Desinagtion Not Found"));
			return getRoleOfDesignation(desination, employeeCapabilityDTOList);
		}
	}

	private Object getRoleOfDesignation(CompanyDesignationInfo desination,
			List<EmployeeCapabilityDTO> employeeCapabilityDTOList) {
		List<EmployeeCapabilityDTO> designationCapabilityDTOList = BeanCopy.objectProperties(desination.getRoles(),
				new TypeReference<List<EmployeeCapabilityDTO>>() {
				});
		return employeeCapabilityDTOList.stream().filter(capability -> capability.getIsEnable().equals(Boolean.TRUE))
				.map(capability -> {
					Optional<EmployeeCapabilityDTO> designationAssigedRole = designationCapabilityDTOList.stream()
							.filter(designationRole -> designationRole.getCapabilityType()
									.equalsIgnoreCase(capability.getCapabilityType()))
							.findAny();
					capability
							.setIsEnable(designationAssigedRole.isPresent() ? designationAssigedRole.get().getIsEnable()
									: Boolean.FALSE);
					List<EmployeeCapabilityDTO> childCapabilityNameList = capability.getChildCapabilityNameList();
					if (childCapabilityNameList != null) {
						capability.setChildCapabilityNameList(childCapabilityNameList.stream()
								.filter(child -> child.getIsEnable().equals(Boolean.TRUE)).map(child -> {
									if (capability.getIsEnable().equals(Boolean.TRUE)
											&& designationAssigedRole.get().getChildCapabilityNameList() != null) {
										Optional<EmployeeCapabilityDTO> fetchedChildRole = designationAssigedRole.get()
												.getChildCapabilityNameList().stream()
												.filter(designationChildRole -> designationChildRole.getCapabilityType()
														.equalsIgnoreCase(child.getCapabilityType()))
												.findAny();
										child.setIsEnable(
												fetchedChildRole.isPresent() ? fetchedChildRole.get().getIsEnable()
														: Boolean.FALSE);
									} else {
										child.setIsEnable(Boolean.FALSE);
									}
									return child;
								}).collect(Collectors.toList()));
					}
					return capability;
				}).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public CompanyDesignationInfoDto updateCompanyDesignation(long companyId,
			CompanyDesignationInfoDto companyDesignationInfoDto) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.TRUE.equals(companyInfo.getIsSubmited())) {
			throw new DesignationCannotUpdate("Designation Cannot Be Updated");
		}

		CompanyDesignationInfo companyDesignationInfo = companyDesignationRepository
				.findByCompanyInfoCompanyIdAndDesignationId(companyId, companyDesignationInfoDto.getDesignationId())
				.orElseThrow(() -> new DesignationIdNotFoundException("No Designation Found To Update!!"));
		Optional<CompanyDesignationInfo> companyDesignationInfos = companyDesignationRepository
				.findByDepartmentAndDesignationNameAndCompanyInfoCompanyId(companyDesignationInfoDto.getDepartment(),
						companyDesignationInfoDto.getDesignationName(), companyId);
		if (companyDesignationInfos.isPresent() && !companyDesignationInfoDto.getDesignationId()
				.equals(companyDesignationInfos.get().getDesignationId())) {
			throw new DuplicateDesignationException("Designation Name Already Exist!!");
		}

		companyDesignationInfo.setDesignationName(companyDesignationInfoDto.getDesignationName());
		companyDesignationInfo.setRoles(companyDesignationInfoDto.getRoles());

		BeanUtils.copyProperties(companyDesignationInfo, companyDesignationInfoDto);
		companyDesignationInfoDto.setIsSubmited(companyInfo.getIsSubmited());
		return companyDesignationInfoDto;
	}

	private CompanyDesignationInfoDto getCompanyDesignationInfoDto(List<CompanyDesignationInfo> companyDesignationInfos,
			CompanyDesignationInfo companyDesignationInfo) {
		CompanyDesignationInfoDto companyDesignationInfoDto = new CompanyDesignationInfoDto();
		BeanUtils.copyProperties(companyDesignationInfo, companyDesignationInfoDto);
		Set<CompanyDesignationInfoDto> childcompanyDesignationInfoDto = new LinkedHashSet<>();
		for (CompanyDesignationInfo companyDesignationInfo2 : companyDesignationInfos) {
			if (companyDesignationInfo2.getParentDesignationInfo().getDesignationId()
					.equals(companyDesignationInfo.getDesignationId())
					&& !companyDesignationInfo2.getParentDesignationInfo().getDesignationId()
							.equals(companyDesignationInfo2.getDesignationId())) {
				childcompanyDesignationInfoDto
						.add(getCompanyDesignationInfoDto(companyDesignationInfos, companyDesignationInfo2));
			}
		}
		companyDesignationInfoDto.setChildcompanyDesignationInfoDto(childcompanyDesignationInfoDto);
		return companyDesignationInfoDto;
	}

	@Override
	public List<CompanyDesignationInfoDto> getAllDepartmentDesignation(long companyId, String departmentName) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));
		List<CompanyDesignationInfo> entityDesignationList = companyDesignationRepository
				.findByCompanyInfoCompanyIdAndDepartment(companyId, departmentName);
		List<CompanyDesignationInfoDto> companyDesignationInfoDtos = new ArrayList<>();
		if (!entityDesignationList.isEmpty() && companyInfo != null) {
			for (CompanyDesignationInfo companyDesignationInfo : entityDesignationList) {
				if (Objects.equals(companyDesignationInfo.getDesignationId(),
						companyDesignationInfo.getParentDesignationInfo().getDesignationId())) {
					companyDesignationInfoDtos
							.add(getCompanyDesignationInfoDto(entityDesignationList, companyDesignationInfo));
				}
			}
		} else {
			return Collections.emptyList();
		}
		companyDesignationInfoDtos.forEach(x -> x.setIsSubmited(companyInfo.getIsSubmited()));
		return companyDesignationInfoDtos;
	}

	private List<Long> ids;

	@Transactional
	@Override
	public String deleteCompanyDesignation(DeleteCompanyDesignationDto deleteCompanyDesignationDto) {
		ids = new ArrayList<>();
		CompanyInfo companyInfo = companyInfoRepository.findById(deleteCompanyDesignationDto.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.FALSE.equals(companyInfo.getIsSubmited()) || companyInfo.getIsSubmited() == null) {
			CompanyDesignationInfo designationInfo = companyDesignationRepository
					.findById(deleteCompanyDesignationDto.getDesignationId())
					.orElseThrow(() -> new DesignationCannotUpdate(DESIGNATION_CANNOT_BE_DELETED));
			List<CompanyDesignationInfo> designationList = companyDesignationRepository
					.findByCompanyInfoCompanyIdAndParentDesignationInfoDesignationId(
							deleteCompanyDesignationDto.getCompanyId(), deleteCompanyDesignationDto.getDesignationId())
					.filter(x -> !x.isEmpty() || designationInfo != null)
					.orElseThrow(() -> new DesignationCannotUpdate(DESIGNATION_CANNOT_BE_DELETED));
			if (!designationList.isEmpty()) {
				List<CompanyDesignationInfo> entityDesignationList = companyDesignationRepository
						.findByCompanyInfoCompanyIdAndDepartment(deleteCompanyDesignationDto.getCompanyId(),
								designationList.get(0).getDepartment());
				for (CompanyDesignationInfo companyDesignationInfo : designationList) {
					deleteCompanyDesignationD(companyDesignationInfo, entityDesignationList);
				}
			}
			ids.add(deleteCompanyDesignationDto.getDesignationId());
			companyDesignationRepository.deleteAllById(ids);
		} else {
			throw new DesignationCannotUpdate(DESIGNATION_CANNOT_BE_DELETED);
		}
		return "Designation Deleted Successfully";
	}

	private void deleteCompanyDesignationD(CompanyDesignationInfo companyDesignationInfo,
			List<CompanyDesignationInfo> entityDesignationList) {
		if (companyDesignationInfo.getParentDesignationInfo() != null && !companyDesignationInfo.getDesignationId()
				.equals(companyDesignationInfo.getParentDesignationInfo().getDesignationId())) {
			for (CompanyDesignationInfo companyDesignationInfo2 : entityDesignationList) {
				if (companyDesignationInfo.getDesignationId()
						.equals(companyDesignationInfo2.getParentDesignationInfo().getDesignationId())) {
					deleteCompanyDesignationD(companyDesignationInfo2, entityDesignationList);
				}
			}
			ids.add(companyDesignationInfo.getDesignationId());
		}
	}

}
