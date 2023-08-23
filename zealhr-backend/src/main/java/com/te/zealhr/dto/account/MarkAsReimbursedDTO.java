package com.te.zealhr.dto.account;

import java.util.ArrayList;

import lombok.Data;

@Data
public class MarkAsReimbursedDTO {
	private ArrayList<Long> reimbursementIdList;

}
