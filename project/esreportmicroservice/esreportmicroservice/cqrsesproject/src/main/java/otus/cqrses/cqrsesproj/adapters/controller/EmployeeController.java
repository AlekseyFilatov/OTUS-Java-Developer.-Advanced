package otus.cqrses.cqrsesproj.adapters.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import otus.cqrses.cqrsesproj.adapters.queries.GetEmployeeByIDQuery;
import otus.cqrses.cqrsesproj.adapters.service.EmployeeCommandService;
import otus.cqrses.cqrsesproj.adapters.service.EmployeeQueryService;
import otus.cqrses.cqrsesproj.application.commands.ChangeAddressCommand;
import otus.cqrses.cqrsesproj.application.commands.ChangeEmailCommand;
import otus.cqrses.cqrsesproj.application.commands.CreateEmployeeCommand;
import otus.cqrses.cqrsesproj.application.dto.ChangeAddressRequestDTO;
import otus.cqrses.cqrsesproj.application.dto.ChangeEmailRequestDTO;
import otus.cqrses.cqrsesproj.application.dto.CreateEmployeeRequestDTO;
import otus.cqrses.cqrsesproj.application.dto.EmployeeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeCommandService commandService;
    private final EmployeeQueryService queryService;

    @GetMapping("{aggregateId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable String aggregateId) {
        final var result = queryService.handle(new GetEmployeeByIDQuery(aggregateId));
        log.info("Get employee result: {}", result);
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/employee")
    public ResponseEntity<String> createEmployee(@Valid @RequestBody CreateEmployeeRequestDTO dto) {
        final var aggregateID = UUID.randomUUID().toString();
        final var id = commandService.handle(new CreateEmployeeCommand(aggregateID, dto.email(), dto.userName(), dto.address()));
        log.info("Created employee id: {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping(path = "/email/{aggregateId}")
    public ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailRequestDTO dto, @PathVariable String aggregateId) {
        commandService.handle(new ChangeEmailCommand(aggregateId, dto.newEmail()));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/address/{aggregateId}")
    public ResponseEntity<Void> changeAddress(@Valid @RequestBody ChangeAddressRequestDTO dto, @PathVariable String aggregateId) {
        commandService.handle(new ChangeAddressCommand(aggregateId, dto.newAddress()));
        return ResponseEntity.ok().build();
    }


}
