package otus.springwebflux.webfluxclient.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//@JsonPropertyOrder
public record CreateEmployeeRequestDTO(
        @Email @NotBlank @Size(min = 10, max = 250) String email,
        @NotBlank @Size(min = 10, max = 250) String address,
        @NotBlank @Size(min = 10, max = 250) String userName) {
}
