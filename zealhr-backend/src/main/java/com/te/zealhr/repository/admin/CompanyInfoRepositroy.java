package com.te.zealhr.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.admin.CompanyInfo;
@Repository
public interface CompanyInfoRepositroy extends JpaRepository<CompanyInfo, Long> {

}
