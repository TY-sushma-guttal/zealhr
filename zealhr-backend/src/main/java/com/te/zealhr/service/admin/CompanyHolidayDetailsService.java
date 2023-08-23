package com.te.zealhr.service.admin;

import java.time.LocalDate;
import java.util.List;

import com.te.zealhr.dto.admin.CompanyHolidayDetailsDto;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;

public interface CompanyHolidayDetailsService {
	
	String addCompanyHoliday(CompanyHolidayDetailsDto companyHolidayDetailsDto,Long companyId,LocalDate selectedDate);
	
	String addListCompanyHoliday(List<CompanyHolidayDetailsDto> companyHolidayDetailsDtos,Long companyId,LocalDate selectedDate);
	
	Long countHolidayPerYear(Long companyId,LocalDate selectedDate);
	
	String updateCompanyHoliday(CompanyHolidayDetailsDto companyHolidayDetailsDtos,Long companyId,LocalDate selectedDate);
	
	String deleteCompanyHoliday(CompanyHolidayDetailsDto companyHolidayDetailsDtos,Long companyId,LocalDate selectedDate);

	List<CompanyHolidayDetailsDto> getAllHolidayPerYear(Long companyId, CanlenderRequestDTO calCanlenderRequestDTO);

	CompanyHolidayDetailsDto getCompanyHoliday(Long companyId, Long holidayId);
}
