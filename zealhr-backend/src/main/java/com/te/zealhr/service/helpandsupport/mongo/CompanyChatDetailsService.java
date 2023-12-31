package com.te.zealhr.service.helpandsupport.mongo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.te.zealhr.dto.helpandsupport.mongo.CompanyChatDetailsDTO;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyChatDetails;

public interface CompanyChatDetailsService {

	public Map<LocalDate, List<CompanyChatDetailsDTO>> getAllChatMessage(Long companyId,String employeeIdOne, String employeeIdTwo);

	public CompanyChatDetails sendMessage(Long companyId,String employeeOne,CompanyChatDetailsDTO companyChatDetailsDTO);

}
