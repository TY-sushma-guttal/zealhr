package com.te.zealhr.dto.account;

import lombok.Data;

@Data
public class SendVendorLinkDTO {
	
	private String email;
	
	private Long companyId;
	
	private String url;

}
