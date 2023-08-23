package com.te.zealhr.service.account;

import java.util.ArrayList;
import java.util.List;

import com.te.zealhr.dto.account.AddressInformationDTO;
import com.te.zealhr.dto.account.AdvanceSalaryByIdDTO;
import com.te.zealhr.dto.account.AdvanceSalaryDTO;
import com.te.zealhr.dto.account.AttachmentsDTO;
import com.te.zealhr.dto.account.DescriptionDTO;
import com.te.zealhr.dto.account.InvoiceDetailsDTO;
import com.te.zealhr.dto.account.InvoiceItemDTO;
import com.te.zealhr.dto.account.InvoiceItemsDTO;
import com.te.zealhr.dto.account.PurchaseInvoiceDTO;
import com.te.zealhr.dto.account.PurchaseInvoiceDetailsByIdDto;
import com.te.zealhr.dto.account.SalesInvoiceDTO;
import com.te.zealhr.dto.account.SalesOrderDropdownDTO;

public interface AccountInvoiceService {



	InvoiceDetailsDTO invoiceDetails(InvoiceDetailsDTO invoiceDetailsDto);

	List<SalesOrderDropdownDTO> salesOrderDropdown(Long companyId);


	InvoiceItemDTO invoiceItems(InvoiceItemDTO invoiceItemsDTO);

	AttachmentsDTO attachments(AttachmentsDTO attachmentsDTO);

	DescriptionDTO description(DescriptionDTO descriptionDto);

	ArrayList<AddressInformationDTO> addressInformation(Long purchaseInvoiceId, Long purchaseOrderId);

	ArrayList<PurchaseInvoiceDTO> purchaseInvoice(Long companyId);

	PurchaseInvoiceDetailsByIdDto purchaseInvoiceById(Long companyId, Long purchaseInvoiceId);

	InvoiceItemDTO invoiceItemsList(Long companyId, Long purchaseOrderId);

	InvoiceItemDTO invoiceItemsListByPurchaseInvoiceId(Long companyId, Long purchaseInvoiceId);

	InvoiceItemDTO salesItemsList(Long companyId, Long salesOrderId);

	InvoiceItemDTO invoiceItemsListBySalesInvoiceId(Long companyId, Long salesInvoiceId);

	ArrayList<AddressInformationDTO> salesAddressInformation(Long salesInvoiceId, Long salesOrderId);

	ArrayList<SalesInvoiceDTO> salesInvoice(Long companyId);

	InvoiceItemDTO salesInvoiceItems(InvoiceItemDTO invoiceItemsDTO);

	PurchaseInvoiceDetailsByIdDto salesInvoiceById(Long companyId, Long salesInvoiceId);

}
