package com.te.zealhr.entity.helpandsupport.mongo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.te.zealhr.dto.helpandsupport.mongo.TicketHistroy;
import com.te.zealhr.audit.Audit;

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
@Document("fa_super_admin_tickets")
public class SuperAdminTickets extends Audit implements Serializable{
	
	@Id
	private String id;

	@Field("cit_it_ticket_id")
	private Long ticketId;
	
	@Field("cit_category")
	private String category;

	@Field("cit_description")
	private String description;

	@Field("cit_attachments_url")
	private String attachmentsUrl;

	@Field("cit_histroy")
	private List<TicketHistroy> ticketHistroys;

	@Field("cit_company_id")
	private Long companyId;
	
	@Field("cit_company_name")
	private String companyName;

}
