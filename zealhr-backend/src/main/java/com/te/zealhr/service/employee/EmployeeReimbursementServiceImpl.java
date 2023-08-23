package com.te.zealhr.service.employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.employee.EmployeeReimbursementDTO;
import com.te.zealhr.dto.employee.EmployeeReimbursementExpenseCategoryDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.entity.admin.CompanyExpenseCategories;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReimbursementInfo;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.InvalidInputException;
import com.te.zealhr.exception.ReimbursementRequestDeleteException;
import com.te.zealhr.repository.admin.CompanyExpenseCategoriesRepository;
import com.te.zealhr.repository.admin.LevelsOfApprovalRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeReimbursementRepository;
import com.te.zealhr.service.hr.CompanyEventDetailsServiceImpl;

@Service
public class EmployeeReimbursementServiceImpl implements EmployeeReimbursementService {

	@Autowired

	CompanyExpenseCategoriesRepository expeRepository;

	@Autowired
	EmployeePersonalInfoRepository personalInfoRepository;

	@Autowired
	EmployeeReimbursementRepository reimbursementRepository;

	@Autowired
	private LevelsOfApprovalRepository approvalRepository;

	@Autowired
	private CompanyEventDetailsServiceImpl eventDetailsServiceImpl;

	@Override
	public EmployeeReimbursementDTO saveEmployeeReimbursement(EmployeeReimbursementDTO reimbursementDTO,
			Long employeeInfoId, Long companyId) {

		EmployeePersonalInfo employeeInfoList = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);

		if (employeeInfoList == null) {
			throw new DataNotFoundException("PersonalInfo Not Found");

		} else {

			if (reimbursementDTO.getExpenseDate().isAfter(LocalDate.now())) {
				throw new InvalidInputException("Invalid Input for the Request");
			}

			EmployeeReimbursementInfo reimbursementInfo = new EmployeeReimbursementInfo();

			BeanUtils.copyProperties(reimbursementDTO, reimbursementInfo);

			reimbursementInfo.setStatus("Pending");

			reimbursementInfo.setEmployeePersonalInfo(employeeInfoList);

			reimbursementInfo.setCompanyExpenseCategories(
					expeRepository.findById(reimbursementDTO.getExpenseCategoryId()).get());

			BeanUtils.copyProperties(reimbursementRepository.save(reimbursementInfo), reimbursementDTO);

			return reimbursementDTO;
		}
	}

	@Override
	public List<EmployeeReimbursementExpenseCategoryDTO> findByExpenseCategoryId(Long companyId) {

		Optional<List<CompanyExpenseCategories>> optional = expeRepository.findByCompanyInfoCompanyId(companyId);

		List<CompanyExpenseCategories> companyExpenseCategoriesList = optional.get();
		List<EmployeeReimbursementExpenseCategoryDTO> employeeReimbursementExpenseCategoryDTO = new ArrayList<>();

		for (CompanyExpenseCategories companyExpenseCategories : companyExpenseCategoriesList) {
			employeeReimbursementExpenseCategoryDTO
					.add(new EmployeeReimbursementExpenseCategoryDTO(companyExpenseCategories.getExpenseCategoryId(),
							companyExpenseCategories.getExpenseCategoryName()));
		}
		return employeeReimbursementExpenseCategoryDTO;
	}

	@Override
	public List<EmployeeReimbursementDTO> getReimbursementDTOList(CanlenderRequestDTO canlenderRequestDTO,
			Long employeeInfoId, Long companyId, String status) {

		EmployeePersonalInfo employeeInfoList = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);

		if (employeeInfoList == null) {
			throw new DataNotFoundException("PersonalInfo Not Found");
		}
		List<EmployeeReimbursementDTO> employeeReimbursementDTOList = new ArrayList<>();

		List<LocalDate> startEndDate = eventDetailsServiceImpl.getStartEndDate(canlenderRequestDTO.getYear(),
				canlenderRequestDTO.getFiscalMonth());
		List<EmployeeReimbursementInfo> employeeReimbursementList = reimbursementRepository
				.findByEmployeePersonalInfoEmployeeInfoIdAndExpenseDateBetween(employeeInfoId, startEndDate.get(0),
						startEndDate.get(1));

		List<EmployeeReimbursementInfo> finalList = new ArrayList<EmployeeReimbursementInfo>();
		if (status.equalsIgnoreCase("All")) {
			finalList.addAll(employeeReimbursementList);
		} else {
			for (EmployeeReimbursementInfo reimbursementInfo : employeeReimbursementList) {
				if (reimbursementInfo.getStatus().equalsIgnoreCase(status))
					finalList.add(reimbursementInfo);
			}
		}
		for (EmployeeReimbursementInfo employeeReimbursementInfo : finalList) {

			EmployeeReimbursementDTO employeeReimbursementDTO = new EmployeeReimbursementDTO();
			LinkedHashMap<String, String> approvedBy = employeeReimbursementInfo.getApprovedBy();
			Set<String> keySet = approvedBy.keySet();
			HashMap<String, String> resultMap = new HashMap<>();
			for (String keyValue : keySet) {
				resultMap.put(keyValue, "Approved");
			}
			if (employeeReimbursementInfo.getRejectedBy() != null) {
				List<String> reimbursementApproval = approvalRepository.findByCompanyInfoCompanyId(companyId).get()
						.getReimbursement();
				if (keySet.size() == 0)
					resultMap.put(reimbursementApproval.get(0), "Rejected");
				else if (keySet.size() == 1)
					resultMap.put(reimbursementApproval.get(1), "Rejected");
				else
					resultMap.put(reimbursementApproval.get(2), "Rejected");
			}
			BeanUtils.copyProperties(employeeReimbursementInfo, employeeReimbursementDTO);
			employeeReimbursementDTO.setApprovedBy(resultMap);
			employeeReimbursementDTO.setExpenseCategoryId(
					employeeReimbursementInfo.getCompanyExpenseCategories().getExpenseCategoryId());
			employeeReimbursementDTO.setExpenseCategoryName(
					employeeReimbursementInfo.getCompanyExpenseCategories().getExpenseCategoryName());
			employeeReimbursementDTOList.add(employeeReimbursementDTO);
		}
		return employeeReimbursementDTOList;
	}

	@Override
	public EmployeeReimbursementDTO getEmployeeReimbursement(Long employeeInfoId, Long reimbursementId,
			Long companyId) {

		EmployeeReimbursementInfo reimbursement = reimbursementRepository
				.findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyId(reimbursementId, companyId);

		if (reimbursement != null) {
			EmployeeReimbursementDTO employeeReimbursementDTO = new EmployeeReimbursementDTO();

			LinkedHashMap<String, String> approvedBy = reimbursement.getApprovedBy();
			Set<String> keySet = approvedBy.keySet();
			HashMap<String, String> resultMap = new HashMap<>();
			for (String keyValue : keySet) {
				resultMap.put(keyValue, "Approved");
			}
			if (reimbursement.getRejectedBy() != null) {
				List<String> reimbursementApproval = approvalRepository.findByCompanyInfoCompanyId(companyId).get()
						.getReimbursement();
				if (keySet.size() == 0)
					resultMap.put(reimbursementApproval.get(0), "Rejected");
				else if (keySet.size() == 1)
					resultMap.put(reimbursementApproval.get(1), "Rejected");
				else
					resultMap.put(reimbursementApproval.get(2), "Rejected");
			}
			BeanUtils.copyProperties(reimbursement, employeeReimbursementDTO);
			employeeReimbursementDTO.setApprovedBy(resultMap);
			employeeReimbursementDTO
					.setExpenseCategoryId(reimbursement.getCompanyExpenseCategories().getExpenseCategoryId());
			employeeReimbursementDTO
					.setExpenseCategoryName(reimbursement.getCompanyExpenseCategories().getExpenseCategoryName());
			return employeeReimbursementDTO;
		} else {
			throw new DataNotFoundException("Data Not Found");
		}
	}

	@Override
	public void deleteReimbursementRequest(Long reimbursementId) {

		Optional<EmployeeReimbursementInfo> reimbursementInfo = reimbursementRepository.findById(reimbursementId);
		if (reimbursementInfo.isPresent()) {

			EmployeeReimbursementInfo employeeReimbursementInfo = reimbursementInfo.get();

			if (employeeReimbursementInfo.getApprovedBy().isEmpty()
					&& employeeReimbursementInfo.getRejectedBy() == null) {
				reimbursementRepository.deleteById(reimbursementId);
			} else {
				throw new ReimbursementRequestDeleteException("Reimbursement Request cannot be Deleted");
			}
		} else {
			throw new DataNotFoundException("Data Not Availalable");
		}
	}

	@Override
	public EmployeeReimbursementDTO editReimbursementRequest(EmployeeReimbursementDTO reimbursementDTO,
			Long reimbursementId) {

		EmployeeReimbursementInfo employeeReimbursementInfo = reimbursementRepository.findById(reimbursementId).get();
		CompanyExpenseCategories category = expeRepository.findById(reimbursementDTO.getExpenseCategoryId())
				.orElseThrow(() -> new DataNotFoundException("Expense category does not exists"));
		if (employeeReimbursementInfo.getApprovedBy().isEmpty() && employeeReimbursementInfo.getRejectedBy() == null) {

			BeanUtils.copyProperties(reimbursementDTO, employeeReimbursementInfo);
			employeeReimbursementInfo.setCompanyExpenseCategories(category);
			employeeReimbursementInfo.setStatus("Pending");

			EmployeeReimbursementDTO reimbursementDTO2 = new EmployeeReimbursementDTO();
			BeanUtils.copyProperties(reimbursementRepository.save(employeeReimbursementInfo), reimbursementDTO2);

			return reimbursementDTO;
		} else {
			throw new ReimbursementRequestDeleteException("Reimbursement Request cannot be Edited");
		}
	}

}
