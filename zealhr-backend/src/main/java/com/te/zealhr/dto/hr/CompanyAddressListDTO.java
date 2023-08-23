package com.te.zealhr.dto.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAddressListDTO {

	private Long companyAddressId;
	
	private String addressDetails;
}
