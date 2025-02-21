package otus.springwebflux.webfluxclient.service;



import java.time.Duration;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import otus.springwebflux.webfluxclient.dto.CreateEmployeeClientRequestDTO;
import otus.springwebflux.webfluxclient.dto.CreateEmployeeRequestDTO;
import otus.springwebflux.webfluxclient.dto.EmployeeAggreegateID;
import otus.springwebflux.webfluxclient.exceptions.EmployeeCreateInEventSourcingRuntimeException;
import otus.springwebflux.webfluxclient.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Slf4j
@Lazy
@RequiredArgsConstructor
@Service
public class WebClientService {

    private final RepositoryService repositoryService;
    private final ApiWebClient apiWebClient;
    private final FileService fileService;

    @RateLimiter(name = "default")
    @CircuitBreaker(name = "default")
    public Mono<DataBuffer> reportEmployees() {
        return apiWebClient.getReport().onErrorResume(ex -> {
            log.error("%nReport not created. Error: %s".formatted(ex.getMessage()));
            throw new EmployeeCreateInEventSourcingRuntimeException("Report not created", ex);}).switchIfEmpty ( Mono.defer(() -> Mono.error ( new ResponseStatusException 
        ( HttpStatus.NOT_FOUND , "Report not created"))));
    }

    @RateLimiter(name = "default")
    @CircuitBreaker(name = "default")
    public Flux<EmployeeAggreegateID> createClientES (CreateEmployeeRequestDTO dto) {
        return Flux.from(apiWebClient.createClient(dto).onErrorResume(ex -> {
            log.error("%nError createClientES() : %s, Employee: %s".formatted(ex.getMessage(), dto.userName()));
            throw new EmployeeCreateInEventSourcingRuntimeException(dto.userName(), ex);}))
            .switchIfEmpty ( Flux.defer(() -> Flux.error ( new ResponseStatusException 
        ( HttpStatus.NOT_FOUND , " Employee %s not create ".formatted(dto.userName()) ))));
    }

    @RateLimiter(name = "default")
    @CircuitBreaker(name = "default")
    public Flux<EmployeeAggreegateID> createClientGRPC (CreateEmployeeClientRequestDTO dto) {
        return Flux.from(apiWebClient.createClientGRPC(dto).onErrorResume(ex -> {
            log.error("%nError createClientGRPC() : %s, Employee: %s".formatted(ex.getMessage(), dto.userName()));
            throw new EmployeeCreateInEventSourcingRuntimeException(dto.userName(), ex);})).switchIfEmpty ( Flux.defer(() -> Flux.error ( new ResponseStatusException 
            ( HttpStatus.NOT_FOUND , " Employee %s not create ".formatted(dto.userName()) ) ) ) );
    }

    @RateLimiter(name = "default")
    @CircuitBreaker(name = "default")
    public Flux<EmployeeAggreegateID> sendfromEmployeeRepository(){
        log.info("%s -> WebClientService.sendfromEmployeeRepository -> %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now()));
        return  Flux.from(repositoryService.getRepo())
                    .flatMap(emp -> Mono.just(new CreateEmployeeRequestDTO(emp.getEmail(), emp.getAddress(), emp.getUserName())))
                    .flatMap(emp -> Mono.from(apiWebClient.createClient(emp).onErrorResume(ex -> {
                        throw new EmployeeCreateInEventSourcingRuntimeException(emp.userName(), ex);
                    })))
                    .doOnNext(msg -> log.info("Id=%s ,%s -> sendfromEmployeeRepository -> %s".formatted(msg.id(), Thread.currentThread().getName(),java.time.LocalTime.now())))
                    .onErrorResume(e -> {
                        log.error("(WebClientService.sendfromEmployeeRepository) Error: " + e.getMessage());
                        return Flux.empty();
                    });} 
      
    @RateLimiter(name = "default")
    @CircuitBreaker(name = "default")                
    public Flux<CreateEmployeeClientRequestDTO> sendfromEmployeeClientRepository(){
                        log.info("%s -> WebClientService.sendfromEmployeeClientRepository -> %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now()));
                        return Flux.from(repositoryService.getRepo())
                                    .flatMap(emp -> Mono.just(new CreateEmployeeRequestDTO(emp.getEmail(), emp.getAddress(), emp.getUserName())))
                                    .flatMap(emp -> Mono.from(apiWebClient.createClient(emp))
                                                    .publishOn(Schedulers.boundedElastic())
                                                    .flatMap(id -> Mono.just(new CreateEmployeeClientRequestDTO(id.id(), emp.email(), emp.address(), emp.userName()))))
                                    .onErrorResume(e -> {
                                        log.error("Error: " + e.getMessage());
                                        return Flux.empty();
                                    });
    }

}
