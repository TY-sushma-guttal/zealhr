package com.te.zealhr.repository.admindept;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

import com.te.zealhr.entity.account.CompanyPurchaseOrder;
import com.te.zealhr.entity.account.CompanySalesOrder;
import com.te.zealhr.entity.it.CompanyPcLaptopDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
/**
 * 
 * @author Brunda
 *
 */
@Repository
public interface CompanyPCLaptopRepository extends JpaRepository<CompanyPcLaptopDetails, String> {

	Optional<CompanyPcLaptopDetails> findBySerialNumberAndCompanyInfoCompanyId(String serialNumber, Long companyId);

	List<CompanyPcLaptopDetails> findByCompanyInfoCompanyId(Long companyId);

	List<CompanyPcLaptopDetails> findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrder(String productName,
			CompanyPurchaseOrder companyPurchaseOrder, CompanySalesOrder companySalesOrder);

	Optional<CompanyPcLaptopDetails> findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
	
	List<CompanyPcLaptopDetails> findByCpldIsWorkingAndCompanyInfoCompanyId(boolean y, Long companyId);
	
}
