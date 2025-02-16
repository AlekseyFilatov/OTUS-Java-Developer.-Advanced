package otus.cqrses.cqrsesproj.application.dto;


public record EmployeeResponseDTO(
        String aggregateId,
        String email,
        String address,
        String userName) {
}