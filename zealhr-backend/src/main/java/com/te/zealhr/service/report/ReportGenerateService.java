package com.te.zealhr.service.report;

import javax.servlet.http.HttpServletResponse;

import com.te.zealhr.dto.report.ReportDTO;
import com.te.zealhr.entity.employee.mongo.EmployeeAttendanceDetails;

public interface ReportGenerateService {
	public boolean exportReport(String reportFormat, HttpServletResponse response, ReportDTO reportDTO, Long companyId,
			Long employeeInfoId);

	public String exportReportMobile(String reportFormat, HttpServletResponse response, ReportDTO reportDTO,
			Long companyId, Long employeeInfoId);

	public EmployeeAttendanceDetails test(String reportFormat, HttpServletResponse response, ReportDTO reportDTO,
			Long companyId, Long employeeInfoId);

	public EmployeeAttendanceDetails test1(String reportFormat, HttpServletResponse response, ReportDTO reportDTO,
			Long companyId, Long employeeInfoId);

	public String exportReportDeleteMobile(ReportDTO reportDTO);
}
