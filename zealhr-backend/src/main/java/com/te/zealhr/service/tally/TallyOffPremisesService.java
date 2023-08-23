package com.te.zealhr.service.tally;

import org.springframework.web.multipart.MultipartFile;

import com.te.zealhr.dto.tally.TallyDetailsDTO;

public interface TallyOffPremisesService {

	public TallyDetailsDTO tallyDetails(MultipartFile master, MultipartFile transaction, String flag, Long companyId);
}
