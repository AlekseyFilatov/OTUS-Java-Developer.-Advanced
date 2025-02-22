package com.otus.grpc.restapi.dto;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;

public record CreateEmployeeRequestDTO(
        String id,
        @Email @NotBlank @Size(min = 10, max = 250) String email,
        @NotBlank @Size(min = 10, max = 250) String address,
        @NotBlank @Size(min = 10, max = 250) String userName) {
}