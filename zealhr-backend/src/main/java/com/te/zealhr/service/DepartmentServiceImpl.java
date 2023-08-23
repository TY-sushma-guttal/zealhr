package com.te.zealhr.service;
//package com.te.zealhr.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.te.zealhr.dto.hr.EventManagementDepartmentNameDTO;
//import com.te.zealhr.entity.Department;
//import com.te.zealhr.entity.admin.CompanyInfo;
//import com.te.zealhr.entity.superadmin.PaymentDetails;
//import com.te.zealhr.entity.superadmin.PlanDetails;
//import com.te.zealhr.exception.EmployeeNotFoundException;
//import com.te.zealhr.repository.DepartmentRepository;
//import com.te.zealhr.repository.admin.CompanyInfoRepository;
//
//@Service
//public class DepartmentServiceImpl implements DepartmentService{
//
//	@Autowired
//	private CompanyInfoRepository companyInfoRepository;
//
//	@Autowired
//	private DepartmentRepository departmentInfoRepository;
//
//	@Override
//	public List<EventManagementDepartmentNameDTO> fetchDepartmentFromPlan(Long companyId) {
//
//		CompanyInfo company = companyInfoRepository.findById(companyId)
//				.orElseThrow(() -> new EmployeeNotFoundException("Company Not Found"));
//		List<Department> departmentsList = departmentInfoRepository.findAll();
//		List<EventManagementDepartmentNameDTO> eventManagementDepartmentList = new ArrayList<>();
//		List<PaymentDetails> paymentDetailsList = company.getPaymentDetailsList();
//		if (!paymentDetailsList.isEmpty()) {
//			PaymentDetails paymentDetails = paymentDetailsList.get(paymentDetailsList.size() - 1);
//			PlanDetails planDetails = paymentDetails.getPlanDetails();
//			if (planDetails != null) {
//				List<String> departments = planDetails.getDepartments();
//				departmentsList.stream().forEach(department -> {
//					if (departments.contains(department.getDepartmentName())) {
//						eventManagementDepartmentList.add(new EventManagementDepartmentNameDTO(
//								department.getDepartmentId(), department.getDepartmentName()));
//					}
//				});
//			}
//		}
//		return eventManagementDepartmentList;
//	}
//
//}
