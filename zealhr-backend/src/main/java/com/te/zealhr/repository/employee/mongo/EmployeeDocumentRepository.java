package com.te.zealhr.repository.employee.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.te.zealhr.dto.employee.mongo.EmployeeDocument;

public interface EmployeeDocumentRepository extends MongoRepository<EmployeeDocument, String> {

}
