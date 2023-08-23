package com.te.zealhr.service.account;

import java.util.List;

import com.te.zealhr.dto.account.AccountPaySlipInputDTO;
import com.te.zealhr.dto.account.AccountPaySlipListDTO;

public interface AccountPaySlipService {

	List<AccountPaySlipListDTO> paySlip(AccountPaySlipInputDTO accountPaySlipInputDTO);


}
