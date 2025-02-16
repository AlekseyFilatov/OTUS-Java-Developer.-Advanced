package otus.cqrses.cqrsesproj.application.mappers;

import otus.cqrses.cqrsesproj.application.dto.EmployeeResponseDTO;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeAggregate;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeDocument;

public final class EmployeeMapper {

    private EmployeeMapper() {
    }


    public static EmployeeResponseDTO employeeResponseDTOFromAggregate(EmployeeAggregate employeeAggregate) {
        return new EmployeeResponseDTO(
            employeeAggregate.getId(),
            employeeAggregate.getEmail(),
            employeeAggregate.getAddress(),
            employeeAggregate.getUserName()
        );
    }

    public static EmployeeResponseDTO employeeResponseDTOFromDocument(EmployeeDocument employeeDocument) {
        return new EmployeeResponseDTO(
            employeeDocument.getAggregateId(),
            employeeDocument.getEmail(),
            employeeDocument.getAddress(),
            employeeDocument.getUserName()
        );
    }

    public static EmployeeDocument employeeDocumentFromAggregate(EmployeeAggregate employeeAggregate) {
        return EmployeeDocument.builder()
                .aggregateId(employeeAggregate.getId())
                .email(employeeAggregate.getEmail())
                .address(employeeAggregate.getAddress())
                .userName(employeeAggregate.getUserName())
                .build();
    }
}