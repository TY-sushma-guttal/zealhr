package com.te.zealhr.controller;
//package com.te.zealhr.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.te.zealhr.audit.BaseConfigController;
//import com.te.zealhr.response.SuccessResponse;
//import com.te.zealhr.service.DepartmentService;
//
//import lombok.RequiredArgsConstructor;
//
//@CrossOrigin(origins = "*")
//@RequiredArgsConstructor
//@RequestMapping(path = "/api/v1/department")
//@RestController
//public class DepartmentController extends BaseConfigController {
//
//	@Autowired
//	private DepartmentService departmentService;
//
//	@GetMapping
//	public ResponseEntity<SuccessResponse> addEmployeeAdvanceSalary(@RequestBody MultipartFile file) {
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Department Fetched Successfully")
//						.data(departmentService.fetchDepartmentFromPlan(getCompanyId())).build());
//	}
//
//}
