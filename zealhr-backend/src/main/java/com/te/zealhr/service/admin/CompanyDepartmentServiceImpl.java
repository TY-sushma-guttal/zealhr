package com.te.zealhr.service.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.zealhr.constants.admin.AdminConstants;
import com.te.zealhr.dto.admin.CompanyDepartmentInfoDTO;
import com.te.zealhr.dto.admin.DeleteCompanyDepartmentDTO;
import com.te.zealhr.entity.admin.CompanyDepartmentDetails;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.exception.CompanyNotFoundException;
import com.te.zealhr.exception.DuplicateDesignationException;
import com.te.zealhr.exception.admin.DesignationCannotUpdate;
import com.te.zealhr.exception.admin.DesignationIdNotFoundException;
import com.te.zealhr.repository.admin.CompanyDepartmentDetailsRepository;
import com.te.zealhr.repository.admin.CompanyInfoRepository;

@Service
public class CompanyDepartmentServiceImpl implements CompanyDepartmentService {

	private static final String DEPARTMENT_CANNOT_BE_DELETED = "Department Cannot Be Deleted";

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private CompanyDepartmentDetailsRepository companyDepartmentDetailsRepository;

	private static final String JSON_FILE_PATH = "/data/roles.json";

//	@Autowired
//	private DepartmentInfoRepository departmentInfoRepository;

	@Override
	public CompanyDepartmentInfoDTO addCompanyDepartment(Long companyId, Long parentDepartmentId,
			CompanyDepartmentInfoDTO companyDepartmentInfoDTO) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.TRUE.equals(companyInfo.getIsSubmited())) {
			throw new DesignationCannotUpdate("Department Cannot Be Updated");
		}

		if (companyDepartmentDetailsRepository.findByCompanyDepartmentNameAndCompanyInfoCompanyId(
				companyDepartmentInfoDTO.getCompanyDepartmentName(), companyId).isPresent()) {
			throw new DuplicateDesignationException("Department Name Already Exist!!");
		}
		CompanyDepartmentDetails companyDepartmentDetails = new CompanyDepartmentDetails();
		BeanUtils.copyProperties(companyDepartmentInfoDTO, companyDepartmentDetails);
		companyDepartmentDetails.setCompanyInfo(companyInfo);

		CompanyDepartmentDetails departmentInfo = companyDepartmentDetailsRepository.findById(parentDepartmentId)
				.orElse(null);

		if (departmentInfo != null) {
			companyDepartmentDetails.setParentDepartmentInfo(departmentInfo);
		} else {
			companyDepartmentDetails.setParentDepartmentInfo(companyDepartmentDetails);
		}

		companyDepartmentDetailsRepository.save(companyDepartmentDetails);
		BeanUtils.copyProperties(companyDepartmentDetails, companyDepartmentInfoDTO);

		return companyDepartmentInfoDTO;
	}

	@Override
	public Object getRoleForDepartment() {	
		ObjectMapper mapper = new ObjectMapper();
		try {
			InputStream transactionReportStream = getClass()
					.getResourceAsStream(JSON_FILE_PATH);
			return mapper.readValue(IOUtils.toString(transactionReportStream, StandardCharsets.UTF_8), Object.class);
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}

	@Transactional
	@Override
	public CompanyDepartmentInfoDTO updateCompanyDepartment(Long companyId,
			CompanyDepartmentInfoDTO companyDepartmentInfoDTO) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.TRUE.equals(companyInfo.getIsSubmited())) {
			throw new DesignationCannotUpdate("Designation Cannot Be Updated");
		}

		CompanyDepartmentDetails companyDepartmentDetails = companyDepartmentDetailsRepository
				.findById(companyDepartmentInfoDTO.getCompanyDepartmentId())
				.orElseThrow(() -> new DesignationIdNotFoundException("No Department Found To Update!!"));
		Optional<CompanyDepartmentDetails> companyDepartmentInfos = companyDepartmentDetailsRepository
				.findByCompanyDepartmentNameAndCompanyInfoCompanyId(companyDepartmentInfoDTO.getCompanyDepartmentName(),
						companyId);
		if (companyDepartmentInfos.isPresent() && !companyDepartmentInfoDTO.getCompanyDepartmentId()
				.equals(companyDepartmentInfos.get().getCompanyDepartmentId())) {
			throw new DuplicateDesignationException("Department Name Already Exist!!");
		}

		companyDepartmentDetails.setCompanyDepartmentName(companyDepartmentInfoDTO.getCompanyDepartmentName());
		companyDepartmentDetails.setCompanyDepartmentRoles(companyDepartmentInfoDTO.getCompanyDepartmentRoles());

		BeanUtils.copyProperties(companyDepartmentDetails, companyDepartmentInfoDTO);
		companyDepartmentInfoDTO.setIsSubmited(companyInfo.getIsSubmited());
		return companyDepartmentInfoDTO;
	}

	private CompanyDepartmentInfoDTO getCompanyDesignationInfoDto(
			List<CompanyDepartmentDetails> companyDepartmentDetailsList,
			CompanyDepartmentDetails companyDepartmentDetails) {
		CompanyDepartmentInfoDTO companyDepartmentInfoDTO = new CompanyDepartmentInfoDTO();
		BeanUtils.copyProperties(companyDepartmentDetails, companyDepartmentInfoDTO);
		Set<CompanyDepartmentInfoDTO> childCompanyDepartmentInfoDTO = new LinkedHashSet<>();
		for (CompanyDepartmentDetails companyDepartment : companyDepartmentDetailsList) {
			if (companyDepartment.getParentDepartmentInfo().getCompanyDepartmentId()
					.equals(companyDepartmentDetails.getCompanyDepartmentId())
					&& !companyDepartment.getParentDepartmentInfo().getCompanyDepartmentId()
							.equals(companyDepartment.getCompanyDepartmentId())) {
				childCompanyDepartmentInfoDTO
						.add(getCompanyDesignationInfoDto(companyDepartmentDetailsList, companyDepartment));
			}
		}
		companyDepartmentInfoDTO.setChildCompanyDepartmentInfoDTO(childCompanyDepartmentInfoDTO);
		return companyDepartmentInfoDTO;
	}

	@Override
	public List<CompanyDepartmentInfoDTO> getAllDepartments(Long companyId) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));
		List<CompanyDepartmentDetails> entityDepartmentList = companyDepartmentDetailsRepository
				.findByCompanyInfoCompanyId(companyId);
		List<CompanyDepartmentInfoDTO> companyDepartmentDTOs = new ArrayList<>();

		for (CompanyDepartmentDetails companyDepartmentDetails : entityDepartmentList) {
			if (Objects.equals(companyDepartmentDetails.getCompanyDepartmentId(),
					companyDepartmentDetails.getParentDepartmentInfo().getCompanyDepartmentId())) {
				companyDepartmentDTOs.add(getCompanyDesignationInfoDto(entityDepartmentList, companyDepartmentDetails));
			}
		}

		companyDepartmentDTOs.forEach(x -> x.setIsSubmited(companyInfo.getIsSubmited()));
		return companyDepartmentDTOs;
	}

	private List<Long> ids;

	@Transactional
	@Override
	public String deleteCompanyDesignation(DeleteCompanyDepartmentDTO deleteCompanyDepartmentDTO) {
		ids = new ArrayList<>();
		CompanyInfo companyInfo = companyInfoRepository.findById(deleteCompanyDepartmentDTO.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.FALSE.equals(companyInfo.getIsSubmited()) || companyInfo.getIsSubmited() == null) {
			CompanyDepartmentDetails companyDepartmentDetails = companyDepartmentDetailsRepository
					.findById(deleteCompanyDepartmentDTO.getCompanyDepartmentId())
					.orElseThrow(() -> new DesignationCannotUpdate(DEPARTMENT_CANNOT_BE_DELETED));
			List<CompanyDepartmentDetails> departmentList = companyDepartmentDetailsRepository
					.findByCompanyInfoCompanyIdAndParentDepartmentInfoCompanyDepartmentId(
							deleteCompanyDepartmentDTO.getCompanyId(),
							deleteCompanyDepartmentDTO.getCompanyDepartmentId())
					.filter(x -> !x.isEmpty() || companyDepartmentDetails != null)
					.orElseThrow(() -> new DesignationCannotUpdate(DEPARTMENT_CANNOT_BE_DELETED));
			if (!departmentList.isEmpty()) {
				List<CompanyDepartmentDetails> entityDepartmentList = companyDepartmentDetailsRepository
						.findByCompanyInfoCompanyId(deleteCompanyDepartmentDTO.getCompanyId());
				for (CompanyDepartmentDetails companyDepartment : departmentList) {
					deleteCompanyDesignationD(companyDepartment, entityDepartmentList);
				}
			}
			ids.add(deleteCompanyDepartmentDTO.getCompanyDepartmentId());
			companyDepartmentDetailsRepository.deleteAllById(ids);
		} else {
			throw new DesignationCannotUpdate(DEPARTMENT_CANNOT_BE_DELETED);
		}
		return "Department Deleted Successfully";
	}

	private void deleteCompanyDesignationD(CompanyDepartmentDetails companyDepartmentDetails,
			List<CompanyDepartmentDetails> entityDepartmentList) {
		if (companyDepartmentDetails.getParentDepartmentInfo() != null
				&& !companyDepartmentDetails.getCompanyDepartmentId()
						.equals(companyDepartmentDetails.getParentDepartmentInfo().getCompanyDepartmentId())) {
			for (CompanyDepartmentDetails companyDepartment : entityDepartmentList) {
				if (companyDepartmentDetails.getCompanyDepartmentId()
						.equals(companyDepartment.getParentDepartmentInfo().getCompanyDepartmentId())) {
					deleteCompanyDesignationD(companyDepartment, entityDepartmentList);
				}
			}
			ids.add(companyDepartmentDetails.getCompanyDepartmentId());
		}
	}

}
