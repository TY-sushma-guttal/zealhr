package com.te.zealhr.service.reportingmanager;

import static com.te.zealhr.common.admin.EmployeeLeaveDetailsConstants.APPROVED;
import static com.te.zealhr.common.admin.EmployeeLeaveDetailsConstants.PENDING;
import static com.te.zealhr.common.admin.EmployeeLeaveDetailsConstants.REJECTED;
import static com.te.zealhr.common.admin.EmployeeLeaveDetailsConstants.STATUS_IS_NOT_VALIED;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.reportingmanager.RegularizationDTO;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeRegularizationDetails;
import com.te.zealhr.entity.employee.mongo.AttendanceDetails;
import com.te.zealhr.entity.employee.mongo.EmployeeAttendanceDetails;
import com.te.zealhr.entity.employee.mongo.EmployeeAttendanceDetails.EmployeeAttendanceDetailsBuilder;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.repository.employee.EmployeeRegularizationDetailsRepository;
import com.te.zealhr.repository.employee.EmployeeReportingInfoRepository;
import com.te.zealhr.repository.employee.mongo.EmployeeAttendanceDetailsRepository;
import com.te.zealhr.service.notification.employee.InAppNotificationServiceImpl;
import com.te.zealhr.service.notification.employee.PushNotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegularizationApprovalServiceImpl implements RegularizationApprovalService {

	private final EmployeeReportingInfoRepository reportingInfoRepository;

	private final InAppNotificationServiceImpl notificationServiceImpl;

	private final PushNotificationService pushNotificationService;

	private final EmployeeRegularizationDetailsRepository employeeRegularizationDetailsRepository;

	private final EmployeeAttendanceDetailsRepository employeeAttendanceDetailsRepository;

	@Override
	public List<RegularizationDTO> getAllRegularizationDetails(Long employeeInfoId, Long companyId) {
		List<RegularizationDTO> regularizationDTOList = new ArrayList<>();
		List<Long> employeeInfoIList = reportingInfoRepository
				.findByReportingManagerEmployeeInfoIdAndReportingManagerCompanyInfoCompanyId(employeeInfoId, companyId)
				.stream().map(employee -> employee.getEmployeePersonalInfo().getEmployeeInfoId())
				.collect(Collectors.toList());
		List<EmployeeRegularizationDetails> regularizationDetailList = employeeRegularizationDetailsRepository
				.findByEmployeePersonalInfoEmployeeInfoIdIn(employeeInfoIList);

		regularizationDetailList.stream().forEach(regularization -> {
			EmployeePersonalInfo employeePersonalInfo = regularization.getEmployeePersonalInfo();
			EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
			if (employeeOfficialInfo != null) {
				regularizationDTOList
						.add(RegularizationDTO.builder().employeeInfoId(employeePersonalInfo.getEmployeeInfoId())
								.employeeId(employeeOfficialInfo.getEmployeeId())
								.department(employeeOfficialInfo.getDepartment())
								.designation(employeeOfficialInfo.getDesignation())
								.fullName(employeePersonalInfo.getFirstName())
								.regularizationId(regularization.getRegularizationId())
								.punchIn(regularization.getPunchIn()).punchOut(regularization.getPunchOut())
								.reason(regularization.getReason()).rejectionReason(regularization.getRejectionReason())
								.status(regularization.getStatus()).build());
			}
		});
		return regularizationDTOList;
	}

	@Override
	@Transactional
	public String updateLeaveStatus(Long companyId, Long employeeInfoId, Long regularizationId,
			AdminApprovedRejectDto adminApprovedRejectDto) {
		String message = "Regularized which was applied for ";
		return employeeRegularizationDetailsRepository.findById(regularizationId)
				.filter(regularizationApplied -> List.of(APPROVED, REJECTED, PENDING)
						.contains(regularizationApplied.getStatus()))
				.filter(regularizationApplied -> regularizationApplied.getStatus().equalsIgnoreCase(PENDING))
				.map(employeeRegularizationApplied -> {
					return Optional.ofNullable(adminApprovedRejectDto.getStatus())
							.filter(status -> status.equalsIgnoreCase(REJECTED)).map(r -> {
								employeeRegularizationApplied.setStatus(adminApprovedRejectDto.getStatus());
								employeeRegularizationApplied.setRejectionReason(adminApprovedRejectDto.getReason());
								notificationServiceImpl.saveNotification(
										message + employeeRegularizationApplied.getPunchIn().toLocalDate()
												+ " is rejected by Reporting Manager",
										employeeRegularizationApplied.getEmployeePersonalInfo().getEmployeeInfoId());

								if (employeeRegularizationApplied.getEmployeePersonalInfo().getExpoToken() != null) {
									pushNotificationService.pushMessage("zealhr",
											message + employeeRegularizationApplied.getPunchIn().toLocalDate()
													+ " is rejected by Reporting Manager",
											employeeRegularizationApplied.getEmployeePersonalInfo().getExpoToken());
								}

								return "Reject Regularization Successful";
							}).orElseGet(() -> Optional.of(adminApprovedRejectDto.getStatus())
									.filter(status -> status.equalsIgnoreCase(APPROVED)).map(a -> {
										employeeRegularizationApplied.setStatus(adminApprovedRejectDto.getStatus());
										updateAttendance(employeeRegularizationApplied, companyId);
										notificationServiceImpl.saveNotification(
												message + employeeRegularizationApplied.getPunchIn().toLocalDate()
														+ " is approved by Reporting Manager",
												employeeRegularizationApplied.getEmployeePersonalInfo()
														.getEmployeeInfoId());

										if (employeeRegularizationApplied.getEmployeePersonalInfo()
												.getExpoToken() != null) {
											pushNotificationService.pushMessage("zealhr",
													message + employeeRegularizationApplied.getPunchIn().toLocalDate()
															+ " is approved by Reporting Manager",
													employeeRegularizationApplied.getEmployeePersonalInfo()
															.getExpoToken());
										}

										return "Approve Regularization Successful";
									}).orElseGet(() -> STATUS_IS_NOT_VALIED));

				}).orElseThrow(() -> new DataNotFoundException("Data Not Available"));
	}

	@Transactional
	private void updateAttendance(EmployeeRegularizationDetails employeeRegularizationDetails, Long companyId) {
		EmployeePersonalInfo employeePersonalInfo = employeeRegularizationDetails.getEmployeePersonalInfo();
		EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
		if (employeeOfficialInfo == null) {
			throw new DataNotFoundException("Employee Official Details Not Found");
		}
		LocalDate localDate = employeeRegularizationDetails.getPunchIn().toLocalDate();

		Optional<EmployeeAttendanceDetails> employeeAttendanceDetails = employeeAttendanceDetailsRepository
				.findByEmployeeInfoIdAndMonthNoAndYear(employeePersonalInfo.getEmployeeInfoId(),
						localDate.getMonthValue(), localDate.getYear());
		if (employeeAttendanceDetails.isPresent()) {
			List<AttendanceDetails> attendanceDetailsList = employeeAttendanceDetails.get().getAttendanceDetails();
			System.err.println(attendanceDetailsList.get(0).getPunchIn().toLocalDate().equals(localDate));
			Optional<AttendanceDetails> attendanceDetails = attendanceDetailsList.stream()
					.filter(dayAttendance -> (dayAttendance.getPunchIn() != null
							&& dayAttendance.getPunchIn().toLocalDate().equals(localDate))
							|| (dayAttendance.getPunchOut() != null && dayAttendance.getPunchOut().toLocalDate()
									.equals(employeeRegularizationDetails.getPunchOut().toLocalDate())))
					.findAny();
			if (attendanceDetails.isPresent()) {
				System.err.println("Hello  came");
				attendanceDetails.get().setIsInsideLocation(Boolean.TRUE);
				attendanceDetails.get().setPunchIn(employeeRegularizationDetails.getPunchIn());
				attendanceDetails.get().setPunchOut(employeeRegularizationDetails.getPunchOut());
				attendanceDetailsList.remove(attendanceDetails.get().getDetailsId()-1);
				attendanceDetailsList.add(attendanceDetails.get().getDetailsId()-1, attendanceDetails.get());
				employeeAttendanceDetails.get().setAttendanceDetails(attendanceDetailsList);
			} else {
				employeeAttendanceDetails.get().getAttendanceDetails()
						.add(AttendanceDetails.builder().punchIn(employeeRegularizationDetails.getPunchIn())
								.punchOut(employeeRegularizationDetails.getPunchOut()).isInsideLocation(Boolean.TRUE)
								.detailsId(attendanceDetailsList.size() + 1).build());
			}
			employeeAttendanceDetailsRepository.save(employeeAttendanceDetails.get());
		} else {
			employeeAttendanceDetailsRepository.save(EmployeeAttendanceDetails.builder()
					.monthNo(localDate.getMonthValue()).year(localDate.getYear())
					.employeeInfoId(employeePersonalInfo.getEmployeeInfoId()).companyId(companyId)
					.attendanceDetails(
							List.of(AttendanceDetails.builder().punchIn(employeeRegularizationDetails.getPunchIn())
									.punchOut(employeeRegularizationDetails.getPunchOut())
									.isInsideLocation(Boolean.TRUE).detailsId(1).build()))
					.build());
		}

	}

}
