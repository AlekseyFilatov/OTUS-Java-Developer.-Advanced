package otus.cqrses.cqrsesproj.application.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;

public record ChangeAddressRequestDTO(@NotBlank @Size(min = 10, max = 250) String newAddress) {
}