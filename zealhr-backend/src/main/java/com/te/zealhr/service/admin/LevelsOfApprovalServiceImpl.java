package com.te.zealhr.service.admin;

import static com.te.zealhr.common.admin.LevelsOfApprovalConstants.COMPANY_NOT_FOUND;
import static com.te.zealhr.common.admin.LevelsOfApprovalConstants.COMPANY_NOT_FOUND_OR_COMPANY_CAN_NOT_UPDATE_THE_RECORD;
import static com.te.zealhr.common.admin.LevelsOfApprovalConstants.LEVELS_OF_APPROVAL_ADDED_SUCCESSFULLY;
import static com.te.zealhr.common.admin.LevelsOfApprovalConstants.LEVELS_OF_APPROVAL_ALREADY_EXIST;
import static com.te.zealhr.common.admin.LevelsOfApprovalConstants.THE_ADD_LEVELS_OF_APPROVAL_METHOD_BEGINS;
import static com.te.zealhr.common.admin.LevelsOfApprovalConstants.THE_ADD_LEVELS_OF_APPROVAL_METHOD_END;
import static com.te.zealhr.common.admin.LevelsOfApprovalConstants.THE_GET_LEVELS_OF_APPROVAL_METHOD_BEGINS;
import static com.te.zealhr.common.admin.LevelsOfApprovalConstants.THE_GET_LEVELS_OF_APPROVAL_METHOD_END;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.te.zealhr.dto.admin.LevelsOfApprovalDto;
import com.te.zealhr.entity.admin.LevelsOfApproval;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.exception.admin.CompanyNotExistException;
import com.te.zealhr.exception.admin.LevelsOfApprovalAlreadyExistException;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.admin.LevelsOfApprovalRepository;
import com.te.zealhr.repository.employee.EmployeePersonelInfoRepository;
import com.te.zealhr.service.notification.employee.InAppNotificationServiceImpl;
import com.te.zealhr.service.notification.employee.PushNotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class LevelsOfApprovalServiceImpl implements LevelsOfApprovalService {

	private final LevelsOfApprovalRepository approvalRepository;

	private final CompanyInfoRepository companyInfoRepository;

	private final InAppNotificationServiceImpl notificationServiceImpl;

	private final PushNotificationService pushNotificationService;

	private final EmployeePersonelInfoRepository employeePersonelInfoRepository;

	@Transactional
	@Override
	public String addLevelsOfApproval(LevelsOfApprovalDto approvalDto, Long companyId) {
		log.info(THE_ADD_LEVELS_OF_APPROVAL_METHOD_BEGINS, approvalDto, " And Company Id", companyId);
		return companyInfoRepository.findByCompanyIdAndIsSubmited(companyId, false).map(companyInfo -> Optional
				.of(companyInfo).filter(company -> companyInfo.getLevelsOfApproval() == null).map(companydetails -> {
					LevelsOfApproval levelsOfApproval = new LevelsOfApproval();
					BeanUtils.copyProperties(approvalDto, levelsOfApproval);
					companyInfo.setIsSubmited(Boolean.TRUE);
					levelsOfApproval.setCompanyInfo(companyInfo);
					approvalRepository.save(levelsOfApproval);
					EmployeePersonalInfo employeePersonalInfo = companyInfo.getEmployeePersonalInfoList().get(0);
					notificationServiceImpl.saveNotification(
							"Company " + companyInfo.getCompanyName() + " Configuration Completed",
							employeePersonalInfo.getEmployeeInfoId());

					if (employeePersonalInfo.getExpoToken() != null) {
						pushNotificationService.pushMessage("zealhr",
								"Company " + companyInfo.getCompanyName() + " Configuration Completed",
								employeePersonalInfo.getExpoToken());
					}

					log.info(THE_ADD_LEVELS_OF_APPROVAL_METHOD_END + LEVELS_OF_APPROVAL_ADDED_SUCCESSFULLY);
					return LEVELS_OF_APPROVAL_ADDED_SUCCESSFULLY;
				}).orElseThrow(() -> {
					log.error(LEVELS_OF_APPROVAL_ALREADY_EXIST);
					return new LevelsOfApprovalAlreadyExistException(LEVELS_OF_APPROVAL_ALREADY_EXIST);
				})).orElseThrow(() -> {
					log.error(COMPANY_NOT_FOUND_OR_COMPANY_CAN_NOT_UPDATE_THE_RECORD);
					return new CompanyNotExistException(COMPANY_NOT_FOUND_OR_COMPANY_CAN_NOT_UPDATE_THE_RECORD);
				});
	}

	@Override
	public LevelsOfApprovalDto getLevelsOfApproval(Long companyId) {
		log.info(THE_GET_LEVELS_OF_APPROVAL_METHOD_BEGINS + companyId);
		return companyInfoRepository.findById(companyId).map(companydetails -> {
			LevelsOfApprovalDto approvalDto = new LevelsOfApprovalDto();
			if (companydetails.getLevelsOfApproval() == null)
				return approvalDto;
			BeanUtils.copyProperties(companydetails.getLevelsOfApproval(), approvalDto);
			log.info(THE_GET_LEVELS_OF_APPROVAL_METHOD_END, approvalDto);
			approvalDto.setIsSubmited(companydetails.getIsSubmited());
			return approvalDto;
		}).orElseThrow(() -> {
			log.error(COMPANY_NOT_FOUND);
			return new CompanyNotExistException(COMPANY_NOT_FOUND);
		});
	}

}
