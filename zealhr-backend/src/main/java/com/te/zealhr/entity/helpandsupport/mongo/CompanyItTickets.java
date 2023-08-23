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
@Document("fa_company_it_tickets")
public class CompanyItTickets extends Audit implements Serializable {
	@Id
	private String id;

	@Field("cit_it_ticket_id")
	private Long ticketId;

	@Field("cit_category")
	private String category;

	@Field("cit_sub_category")
	private String subCategory;

	@Field("cit_description")
	private String description;

	@Field("cit_employee_id")
	private String employeeId;

	@Field("cit_attachments_url")
	private String attachmentsUrl;

	@Field("cit_product_name")
	private String productName;

	@Field("cit_identification_number")
	private String identificationNumber;

	@Field("cit_histroy")
	private List<TicketHistroy> ticketHistroys;

	@Field("cit_feedback")
	private String feedback;

	@Field("cit_rating")
	private Integer rating;

	@Field("cit_company_id")
	private Long companyId;

	@Convert(converter = MapToStringConverter.class)
	@Field("cit_question_answer")
	private Map<String, String> questionAnswer;
}
