package com.te.zealhr.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.te.zealhr.dto.admin.BussinessPlanDTO;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.admin.CompanyRuleInfo;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.admin.CompanyRuleRepository;
import com.te.zealhr.util.CacheStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServerCommandLineRunner implements CommandLineRunner {

	private final SocketIOServer server;

	private final CacheStore<CompanyRuleInfo> companyRuleCacheStore;

	private final CacheStore<CompanyInfo> companyCacheStore;

	private final CompanyInfoRepository companyInfoRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		log.info("Server Start Successfully");
		server.startAsync();
		companyInfoRepository.findAll().stream().forEach(company -> {
			companyCacheStore.add(company.getCompanyId().toString(), company);
			Hibernate.initialize(company.getCompanyRuleInfo());
			companyRuleCacheStore.add(company.getCompanyId().toString(), company.getCompanyRuleInfo());
		});

	}

}