package com.te.zealhr.service.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.te.zealhr.dto.account.AccountClientDetailsDTO;
import com.te.zealhr.dto.account.ClientContactDetailsDTO;
import com.te.zealhr.dto.account.SendVendorLinkDTO;
import com.te.zealhr.dto.account.VendorBasicDetailsDTO;
import com.te.zealhr.dto.account.VendorContactDetailsDTO;
import com.te.zealhr.dto.account.VendorDetailsDTO;
import com.te.zealhr.dto.account.VendorFormDTO;
import com.te.zealhr.dto.account.VendorListDTO;
import com.te.zealhr.dto.account.mongo.ContactPerson;
import com.te.zealhr.dto.employee.MailDto;
import com.te.zealhr.entity.account.mongo.CompanyVendorInfo;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.admin.mongo.VendorFormConfiguration;
import com.te.zealhr.entity.sales.ClientContactPersonDetails;
import com.te.zealhr.entity.sales.CompanyClientInfo;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.account.CustomExceptionForAccount;
import com.te.zealhr.exception.account.VendorAlreadyExistException;
import com.te.zealhr.repository.account.CompanyVendorInfoRepository;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.admin.mongo.VendorFormConfigurationRepository;
import com.te.zealhr.repository.sales.CompanyClientInfoRepository;
import com.te.zealhr.service.mail.employee.EmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VendorManagementServiceImpl implements VendorManagementService {

	@Autowired
	private VendorFormConfigurationRepository vendorFormConfigurationRepository;

	@Autowired
	private CompanyVendorInfoRepository companyVendorInfoRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CompanyClientInfoRepository clientInfoRepo;

	@Override
	public List<VendorFormDTO> getDynamicFactors(Long companyId) {
		List<VendorFormDTO> vendorFormDTOList = new ArrayList<>();
		Optional<VendorFormConfiguration> vendorFormConfiguration = vendorFormConfigurationRepository
				.findByCompanyId(companyId);
		if (!vendorFormConfiguration.isPresent()) {
			return vendorFormDTOList;
		} else {
			VendorFormConfiguration vendorForm = vendorFormConfiguration.get();
			Map<String, String> attributes = vendorForm.getAttributes();
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				vendorFormDTOList.add(new VendorFormDTO(entry.getKey(), entry.getValue()));
			}
		}
		return vendorFormDTOList;
	}

	@Override
	@Transactional
	public VendorDetailsDTO saveVendorDetails(VendorDetailsDTO vendorDetailsDTO) {
		CompanyInfo companyInfo = companyInfoRepository.findById(vendorDetailsDTO.getCompanyId())
				.orElseThrow(() -> new DataNotFoundException("Company Not Found"));
		if (!companyVendorInfoRepository
				.findByVendorNameAndCompanyId(vendorDetailsDTO.getVendorName(), companyInfo.getCompanyId()).isEmpty()) {
			throw new VendorAlreadyExistException("Vendor Already Exists");
		}
		CompanyVendorInfo companyVendorInfo = new CompanyVendorInfo();
		BeanUtils.copyProperties(vendorDetailsDTO, companyVendorInfo);
		companyVendorInfo.setVendorAddress(vendorDetailsDTO.getVendorAddress());
		companyVendorInfo.setContactPersons(vendorDetailsDTO.getContactPersons());
		companyVendorInfo.setVendorBankDetails(vendorDetailsDTO.getVendorBankDetails());
		companyVendorInfo.setOtherDetails(vendorDetailsDTO.getOtherDetails());
		companyVendorInfoRepository.save(companyVendorInfo);
		return vendorDetailsDTO;
	}

	@Override
	public List<VendorBasicDetailsDTO> getVendorBasicDetails(Long companyId) {
		List<VendorBasicDetailsDTO> vendorBasicDetailsDTOList = new ArrayList<>();
		List<CompanyVendorInfo> companyVendorInfoList = companyVendorInfoRepository.findByCompanyId(companyId);
		for (CompanyVendorInfo companyVendorInfo : companyVendorInfoList) {
			VendorBasicDetailsDTO vendorBasicDetailsDTO = new VendorBasicDetailsDTO();
			BeanUtils.copyProperties(companyVendorInfo, vendorBasicDetailsDTO);
			List<ContactPerson> contactPersons = companyVendorInfo.getContactPersons();
			if (contactPersons != null && !contactPersons.isEmpty()) {
				vendorBasicDetailsDTO.setContactPersonName(contactPersons.get(0).getContactPersonName());
				vendorBasicDetailsDTO.setEmailId(contactPersons.get(0).getEmailId());
				vendorBasicDetailsDTO.setMobileNumber(contactPersons.get(0).getMobileNumber());
			}
			vendorBasicDetailsDTOList.add(vendorBasicDetailsDTO);
		}
		Collections.reverse(vendorBasicDetailsDTOList);
		return vendorBasicDetailsDTOList;
	}

	@Override
	public VendorDetailsDTO getVendorDetailsById(String vendorId) {
		CompanyVendorInfo vendorInfo = companyVendorInfoRepository.findById(vendorId)
				.orElseThrow(() -> new DataNotFoundException("No Vendor Details Found"));
		VendorDetailsDTO vendorDetailsDTO = new VendorDetailsDTO();
		BeanUtils.copyProperties(vendorInfo, vendorDetailsDTO);
		vendorDetailsDTO.setVendorAddress(vendorInfo.getVendorAddress());
		vendorDetailsDTO.setContactPersons(vendorInfo.getContactPersons());
		vendorDetailsDTO.setVendorBankDetails(vendorInfo.getVendorBankDetails());
		vendorDetailsDTO.setOtherDetails(vendorInfo.getOtherDetails());
		return vendorDetailsDTO;
	}

	@Override
	public String sendLink(SendVendorLinkDTO sendVendorLinkDTO, Long userId) {
		CompanyInfo companyInfo = companyInfoRepository.findById(sendVendorLinkDTO.getCompanyId())
				.orElseThrow(() -> new DataNotFoundException("Company Not Found"));
		if (sendVendorLinkDTO.getEmail() != null) {
			MailDto mailDto = new MailDto();
			mailDto.setTo(sendVendorLinkDTO.getEmail());
			mailDto.setSubject("Vendor Registration");
			mailDto.setBody("<html>\n" + "<body>\n" + "\n" + "Dear Vendor" + ",<BR />" + "<BR />"
					+ "Please provide your information by filling out the form below" + "<BR />" + "<BR />"
					+ "<a href='" + sendVendorLinkDTO.getUrl() + "/" + sendVendorLinkDTO.getCompanyId() + "/" + userId
					+ "'>" + "Vendor Details Form" + "</a>" + "<BR />" + "<BR />" + "Thanks and Regards," + "<BR />"
					+ "Team " + companyInfo.getCompanyName() + "</body>\n" + "</html>");
			emailService.sendMailWithLink(mailDto);
		}
		return "Link Sent Successfully";
	}

	@Override
	public ArrayList<VendorListDTO> vendorList(Long companyId) {
		ArrayList<VendorListDTO> vendorinfo = new ArrayList<>();
		List<CompanyVendorInfo> vendorDetails = companyVendorInfoRepository.findByCompanyId(companyId);
		if (vendorDetails == null || vendorDetails.isEmpty()) {
			return vendorinfo;
		}
		log.info("Vendor details verified");

		for (CompanyVendorInfo companyVendorInfo : vendorDetails) {
			VendorListDTO vendorListDTO = new VendorListDTO();
			vendorListDTO.setVendorInfoId(companyVendorInfo.getVendorInfoId());
			vendorListDTO.setVendorName(companyVendorInfo.getVendorName());
			vendorListDTO.setId(companyVendorInfo.getId());
			vendorinfo.add(vendorListDTO);
		}
		return vendorinfo;
	}

	@Override
	public VendorContactDetailsDTO contactDetails(Long companyId, String id) {
		List<CompanyVendorInfo> contactDetails = companyVendorInfoRepository.findByIdAndCompanyId(id, companyId);
		if (contactDetails == null || contactDetails.isEmpty()) {
			throw new CustomExceptionForAccount("Vendor contact  details not found ");
		}
		log.info("Vendor contact details verified");
		VendorContactDetailsDTO vendorContactDetailsDTO = new VendorContactDetailsDTO();
		vendorContactDetailsDTO
				.setContactPersonName(contactDetails.get(0).getContactPersons().get(0).getContactPersonName());
		vendorContactDetailsDTO.setEmailId(contactDetails.get(0).getContactPersons().get(0).getEmailId());
		vendorContactDetailsDTO.setMobileNumber(contactDetails.get(0).getContactPersons().get(0).getMobileNumber());
		return vendorContactDetailsDTO;
	}

	@Override
	public ArrayList<AccountClientDetailsDTO> clientDetails(Long companyId) {
		ArrayList<AccountClientDetailsDTO> clientInfoDto = new ArrayList<>();
		List<CompanyClientInfo> clientInfo = clientInfoRepo.findByCompanyInfoCompanyId(companyId);
		if (clientInfo == null || clientInfo.isEmpty()) {
			return clientInfoDto;
		}

		for (CompanyClientInfo companyClientInfo : clientInfo) {
			AccountClientDetailsDTO accountClientDetailsDTO = new AccountClientDetailsDTO();

			accountClientDetailsDTO.setClientId(companyClientInfo.getClientId());
			accountClientDetailsDTO.setClientName(companyClientInfo.getClientName());

			ArrayList<ClientContactDetailsDTO> contactInfo = new ArrayList<>();

			List<ClientContactPersonDetails> detailsList = companyClientInfo.getClientContactPersonDetailsList();
			for (ClientContactPersonDetails clientContactinfo : detailsList) {
				ClientContactDetailsDTO clientContactDetailsDTO = new ClientContactDetailsDTO();
				clientContactDetailsDTO.setContactPersonId(clientContactinfo.getContactPersonId());
				clientContactDetailsDTO.setFirstName(clientContactinfo.getFirstName());
				contactInfo.add(clientContactDetailsDTO);
			}
			accountClientDetailsDTO.setContactPersonList(contactInfo);
			clientInfoDto.add(accountClientDetailsDTO);
		}
		return clientInfoDto;
	}

	@Transactional
	@Override
	public VendorDetailsDTO updatePaymentDetails(VendorDetailsDTO vendorDetailsDTO) {
		CompanyVendorInfo vendorInfo = companyVendorInfoRepository.findById(vendorDetailsDTO.getId())
				.orElseThrow(() -> new DataNotFoundException("Vendor Not Found"));
		vendorInfo.setAmountPaid(vendorDetailsDTO.getAmountPaid());
		vendorInfo.setAmountToBePaid(vendorDetailsDTO.getAmountToBePaid());
		vendorInfo.setPaymentDate(vendorDetailsDTO.getPaymentDate());
		vendorInfo.setModeOfPayment(vendorDetailsDTO.getModeOfPayment());
		companyVendorInfoRepository.save(vendorInfo);
		return vendorDetailsDTO;
	}

}
