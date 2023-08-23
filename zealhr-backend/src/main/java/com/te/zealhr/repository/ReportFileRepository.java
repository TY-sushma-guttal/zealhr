package com.te.zealhr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.zealhr.entity.ReportFile;

public interface ReportFileRepository extends MongoRepository<ReportFile, String> {
	Optional<ReportFile> findByReportId(Long reportId);
}
