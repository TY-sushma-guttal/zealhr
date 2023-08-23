package com.te.zealhr.util;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.te.zealhr.dto.admin.BussinessPlanDTO;
import com.te.zealhr.dto.admin.PlanDTO;
import com.te.zealhr.dto.employee.Registration;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.admin.CompanyRuleInfo;
import com.te.zealhr.entity.employee.EmployeeLoginInfo;

@Configuration
public class CacheStoreBeans {

	@Bean
	public CacheStore<EmployeeLoginInfo> cacheStoreEmployeeLogin() {
		return new CacheStore<>(15, TimeUnit.MINUTES);
	}
	
	@Bean
	public CacheStore<Long> cacheStoreOTP() {
		return new CacheStore<>(5, TimeUnit.MINUTES);
	}
	
	@Bean
	public CacheStore<Boolean> cacheStoreValidOTP() {
		return new CacheStore<>(12, TimeUnit.MINUTES);
	}

	@Bean
	public CacheStore<Registration> cacheStoreEmployeeRegistrationDto() {
		return new CacheStore<>(15, TimeUnit.MINUTES);
	}
	
	@Bean
	public CacheStore<BussinessPlanDTO> cacheStorebussinessPlanDTO() {
		return new CacheStore<>(20, TimeUnit.MINUTES);
	}
	
	@Bean
	public CacheStore<PlanDTO> cacheStorePlanDTO() {
		return new CacheStore<>(1, TimeUnit.HOURS);
	}
	
	@Bean
	public CacheStore<CompanyRuleInfo> cacheStoreCompanyRule() {
		return new CacheStore<>();
	}
	
	@Bean
	public CacheStore<CompanyInfo> cacheStoreCompany() {
		return new CacheStore<>();
	}
}