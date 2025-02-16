package com.otus.grpc.restapi.controller;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




import com.otus.grpc.Service.ShopClient;
import com.otus.grpc.restapi.dto.CreateEmployeeRequestDTO;
import com.otus.grpc.restapi.exception.GRPCRuntimeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(path = "/api")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

       
        private final ShopClient shopServer;
    
        @PostMapping(path = "/employee")
        public ResponseEntity<String> createEmployee(@Valid @RequestBody CreateEmployeeRequestDTO dto) {
             try{
                shopServer.createUser(dto.id(), dto.userName(), dto.email());
                return ResponseEntity.status(HttpStatus.CREATED).body(dto.id());
             }
             catch(Exception e)
             {
                log.error("GRPCServerRuntimeException: " + e.getMessage());
			    throw new GRPCRuntimeException(dto.id(), e);
             }             
    }
}
