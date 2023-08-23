package com.te.zealhr.service.hr;

import com.te.zealhr.dto.hr.AddFeedbackDTO;
import com.te.zealhr.dto.hr.AddInterviewRoundDto;
import com.te.zealhr.dto.hr.CompanyChecklistDTO;
import com.te.zealhr.dto.hr.ConfigurationDto;
import com.te.zealhr.dto.hr.EditInterviewRoundDto;


public interface HrConfigurationService {
	AddInterviewRoundDto addInterviewRounds(AddInterviewRoundDto interviewdetails);

	AddFeedbackDTO addFeedback(AddFeedbackDTO feedback);

	CompanyChecklistDTO checklistFactor(CompanyChecklistDTO checklistFactor);

	
	AddInterviewRoundDto editInterviewRoundDetails(EditInterviewRoundDto interviewdetails);

	AddInterviewRoundDto deleteInterviewRound(Long companyId, String roundName);

	AddFeedbackDTO editFeedback(AddFeedbackDTO feedback);

	AddFeedbackDTO deleteFeedbackFactor(Long companyId, String feedbackFactor,String feedbackType);

	CompanyChecklistDTO editChecklistFactor(CompanyChecklistDTO checklistFactor);

	CompanyChecklistDTO deleteChecklistFactor(Long companyId, String checklistFactor);

	ConfigurationDto configuration(long companyId);

	AddInterviewRoundDto interviewRoundList(Long companyId);

	AddFeedbackDTO entryFeedbackFactor(Long companyId, String feedbackType);

}
