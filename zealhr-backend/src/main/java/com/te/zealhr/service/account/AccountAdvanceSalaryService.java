package com.te.zealhr.service.account;

import java.util.ArrayList;

import com.te.zealhr.dto.account.AdvanceSalaryByIdDTO;
import com.te.zealhr.dto.account.AdvanceSalaryDTO;

public interface AccountAdvanceSalaryService {

	ArrayList<AdvanceSalaryDTO> advanceSalary(Long companyId);

	AdvanceSalaryByIdDTO advanceSalaryById(Long advanceSalaryId, Long companyId);

}
