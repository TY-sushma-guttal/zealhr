package com.te.zealhr.repository.account;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.zealhr.dto.account.mongo.ContactPerson;

public interface ContactPersonRepository extends MongoRepository<ContactPerson, String>{

}
