package com.otus.docum.reportservice.entities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Employee")
public class EmployeeDocument {

    @Schema(description = "Optional field, id")
    @BsonProperty(value = "_id")
    private String id;

    @Schema(description = "Optional field, aggregateId")
    @BsonProperty(value = "aggregateId")
    private String aggregateId;

    @Schema(description = "email")
    @BsonProperty(value = "email")
    private String email;

    @Schema(description = "userName")
    @BsonProperty(value = "userName")
    private String userName;

    @Schema(description = "address")
    @BsonProperty(value = "address")
    private String address;

}