package com.otus.docum.reportservice.adapters.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.otus.docum.reportservice.entities.domain.EmployeeDocument;
import com.otus.docum.reportservice.adapters.repository.EmployeeMongoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Lazy
@Service
public class StoreService  {

    private final EmployeeMongoRepository mongoRepository;

    public List<EmployeeDocument> getEmployeeList() {
        return mongoRepository.findAll();
    }

}