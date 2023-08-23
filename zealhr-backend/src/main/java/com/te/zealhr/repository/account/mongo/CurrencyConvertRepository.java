package com.te.zealhr.repository.account.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.te.zealhr.entity.account.mongo.CurrencyConvert;

public interface CurrencyConvertRepository extends MongoRepository<CurrencyConvert, String> {

}
