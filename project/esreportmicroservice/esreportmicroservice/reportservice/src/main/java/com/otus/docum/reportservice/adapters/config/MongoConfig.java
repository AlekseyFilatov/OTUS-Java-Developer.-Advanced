package com.otus.docum.reportservice.adapters.config;

import org.bson.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import com.mongodb.client.MongoCollection;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.otus.docum.reportservice.entities.domain.EmployeeDocument;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MongoConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void mongoInit() {
        final MongoCollection<Document> Employees = mongoTemplate.getCollection("Employees");
        final String aggregateIdIndex = mongoTemplate.indexOps(EmployeeDocument.class).ensureIndex(new Index("aggregateId", Sort.Direction.ASC).unique());
        final var indexInfo = mongoTemplate.indexOps(EmployeeDocument.class).getIndexInfo();
        log.info("MongoDB connected, Employees aggregateId index created: {}", indexInfo);
    }
}