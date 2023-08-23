package com.te.zealhr.repository.it;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.it.CompanyPcLaptopDetails;

@Repository
public interface ITPcLaptopRepository extends JpaRepository<CompanyPcLaptopDetails, String> {

	List<CompanyPcLaptopDetails> findByCompanyInfoCompanyId(Long companyId);

	Optional<CompanyPcLaptopDetails> findBySerialNumberAndCompanyInfoCompanyId(String serialNumber, Long companyId);

	List<CompanyPcLaptopDetails> findByCompanyInfoCompanyIdAndCpldIsWorking(Long companyId, Boolean cpldIsWorking);

}
