package com.te.zealhr.dto.account;

import java.util.List;

import lombok.Data;

@Data
public class AccountClientDetailsDTO {
	private Long clientId;
	private String clientName;
	private List<ClientContactDetailsDTO> contactPersonList;

}
