package otus.cqrses.cqrsesproj.adapters.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeAggregate;
import otus.cqrses.cqrsesproj.application.commands.ChangeAddressCommand;
import otus.cqrses.cqrsesproj.application.commands.ChangeEmailCommand;
import otus.cqrses.cqrsesproj.application.commands.CreateEmployeeCommand;
import otus.cqrses.cqrsesproj.application.event.EventStoreDB;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;


@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeCommandHandler implements EmployeeCommandService{

    private final EventStoreDB eventStoreDB;
    private static final String SERVICE_NAME = "microservice";

@Override
@NewSpan
@Retry(name = SERVICE_NAME)
@CircuitBreaker(name = SERVICE_NAME)
public String handle(@SpanTag("command") CreateEmployeeCommand command) {
    final var aggregate = new EmployeeAggregate(command.aggregateID());
    aggregate.createEmployee(command.email(), command.address(), command.userName());
    eventStoreDB.save(aggregate);

    log.info("(CreateEmployeeCommand) aggregate: {}", aggregate);
    return aggregate.getId();
}

@Override
@NewSpan
@Retry(name = SERVICE_NAME)
@CircuitBreaker(name = SERVICE_NAME)
public void handle(@SpanTag("command") ChangeEmailCommand command) {
    final var aggregate = eventStoreDB.load(command.aggregateID(), EmployeeAggregate.class);
    aggregate.changeEmail(command.newEmail());
    eventStoreDB.save(aggregate);
    log.info("(ChangeEmailCommand) aggregate: {}", aggregate);
}

@Override
@NewSpan
@Retry(name = SERVICE_NAME)
@CircuitBreaker(name = SERVICE_NAME)
public void handle(@SpanTag("command") ChangeAddressCommand command) {
    final var aggregate = eventStoreDB.load(command.aggregateID(), EmployeeAggregate.class);
    aggregate.changeAddress(command.newAddress());
    eventStoreDB.save(aggregate);
    log.info("(ChangeAddressCommand) aggregate: {}", aggregate);
}

}