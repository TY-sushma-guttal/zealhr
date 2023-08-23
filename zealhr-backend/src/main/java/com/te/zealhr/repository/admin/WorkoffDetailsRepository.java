package com.te.zealhr.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.te.zealhr.entity.admin.WorkOffDetails;
import com.te.zealhr.responsedto.admin.WorkOffDetailsResponseDto;

public interface WorkoffDetailsRepository extends JpaRepository<WorkOffDetails,Long> {

//	@Query(value="select new com.te.zealhr.responsedto.admin.WorkoffDetailsResponseDto(wod.weekId,wod.weekNumber,wod.fullDayWorkoff,wod.halfDayWorkoff,wod.companyWorkWeekRule.workWeekRuleId) from WorkoffDetails wod where wod.companyWorkWeekRule.workWeekRuleId in :workWeekRuleIds")
//	List<WorkOffDetailsResponseDto> getAllWorkoffDetails(@Param("workWeekRuleIds") List<Long> workWeekRuleIds);

	public List<WorkOffDetails> findByCompanyWorkWeekRuleWorkWeekRuleIdIn(List<Long> workWeekRuleIds);

}
