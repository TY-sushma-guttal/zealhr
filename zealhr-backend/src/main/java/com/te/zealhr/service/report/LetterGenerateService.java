package com.te.zealhr.service.report;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.te.zealhr.dto.hr.EmployeeBasicDetailsDTO;
import com.te.zealhr.dto.hr.LetterDetailsDTO;
import com.te.zealhr.dto.report.LetterDTO;

public interface LetterGenerateService {
	
	String generateLatter(LetterDTO latterDTO);
	
	List<EmployeeBasicDetailsDTO> getEmployeeDetails(Long companyId);
	
	LetterDetailsDTO saveLetter(LetterDetailsDTO letterDetailsDTO, MultipartFile file, Long companyId, Long userId);
	
	List<LetterDetailsDTO> getEmployeeLetters(Long employeeInfoId, Long companyId);

}
