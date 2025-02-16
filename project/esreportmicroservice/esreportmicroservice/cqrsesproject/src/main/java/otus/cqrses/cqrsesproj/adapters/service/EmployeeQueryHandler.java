package otus.cqrses.cqrsesproj.adapters.service;

import otus.cqrses.cqrsesproj.adapters.queries.GetEmployeeByIDQuery;
import otus.cqrses.cqrsesproj.adapters.repository.EmployeeMongoRepository;
import otus.cqrses.cqrsesproj.application.dto.EmployeeResponseDTO;
import otus.cqrses.cqrsesproj.application.event.EventStoreDB;
import otus.cqrses.cqrsesproj.application.mappers.EmployeeMapper;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeAggregate;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeDocument;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeQueryHandler implements EmployeeQueryService {

    private final EventStoreDB eventStoreDB;
    private final EmployeeMongoRepository mongoRepository;
    private static final String SERVICE_NAME = "microservice";

    @Override
    @NewSpan
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public EmployeeResponseDTO handle(@SpanTag("query") GetEmployeeByIDQuery query) {
        Optional<EmployeeDocument> optionalDocument = mongoRepository.findByAggregateId(query.aggregateID());
        if (optionalDocument.isPresent()) {
            return EmployeeMapper.employeeResponseDTOFromDocument(optionalDocument.get());
        }

        final var aggregate = eventStoreDB.load(query.aggregateID(), EmployeeAggregate.class);
        final var savedDocument = mongoRepository.save(EmployeeMapper.employeeDocumentFromAggregate(aggregate));
        log.info("(GetEmployeeByIDQuery) savedDocument: {}", savedDocument);

        final var employeeResponseDTO = EmployeeMapper.employeeResponseDTOFromAggregate(aggregate);
        log.info("(GetEmployeeByIDQuery) response: {}", employeeResponseDTO);
        return employeeResponseDTO;
    }

}
