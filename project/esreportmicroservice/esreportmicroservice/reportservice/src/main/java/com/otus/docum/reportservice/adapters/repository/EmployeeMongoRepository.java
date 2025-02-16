package com.otus.docum.reportservice.adapters.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.otus.docum.reportservice.entities.domain.EmployeeDocument;

import java.util.Optional;

public interface EmployeeMongoRepository extends MongoRepository<EmployeeDocument, String> {

    Optional<EmployeeDocument> findByAggregateId(String aggregateId);

}