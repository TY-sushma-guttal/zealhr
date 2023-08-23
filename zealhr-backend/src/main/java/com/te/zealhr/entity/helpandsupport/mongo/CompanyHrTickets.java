package com.te.zealhr.entity.helpandsupport.mongo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Convert;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.te.zealhr.dto.helpandsupport.mongo.TicketHistroy;
import com.te.zealhr.audit.Audit;
import com.te.zealhr.util.MapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("fa_company_hr_tickets")
public class CompanyHrTickets extends Audit implements Serializable {

	@Id
	private String ticketObjectId;

	@Field("cht_hr_ticket_id")
	private Long ticketId;

	@Field("cht_category")
	private String category;

	@Field("cht_sub_category")
	private String subCategory;

	@Field("cht_description")
	private String description;

	@Field("cht_employee_id")
	private String employeeId;

	@Field("cht_attachments_url")
	private String attachmentsUrl;

	@Field("cht_histroy")
	private List<TicketHistroy> ticketHistroys;

	@Field("cht_feedback")
	private String feedback;

	@Field("cht_rating")
	private Integer rating;

	@Field("cht_company_id")
	private Long companyId;

	@Convert(converter = MapToStringConverter.class)
	@Field("cht_question_answer")
	private Map<String, String> questionAnswer;

}
