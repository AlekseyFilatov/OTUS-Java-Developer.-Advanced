package otus.springwebflux.webfluxclient.service;



import java.time.Duration;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import otus.springwebflux.webfluxclient.dto.CreateEmployeeClientRequestDTO;
import otus.springwebflux.webfluxclient.dto.CreateEmployeeRequestDTO;
import otus.springwebflux.webfluxclient.dto.EmployeeAggreegateID;
import otus.springwebflux.webfluxclient.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Lazy
@RequiredArgsConstructor
@Service
public class WebClientService {

    private final RepositoryService repositoryService;
    private final ApiWebClient apiWebClient;
    private final FileService fileService;

    public Mono<DataBuffer> reportEmployees() {
        return apiWebClient.getReport();
    }

    public Flux<EmployeeAggreegateID> createClientES (CreateEmployeeRequestDTO dto) {
        return Flux.from(apiWebClient.createClient(dto));
    }

    public Flux<EmployeeAggreegateID> createClientGRPC (CreateEmployeeClientRequestDTO dto) {
        return Flux.from(apiWebClient.createClientGRPC(dto));
    }

    public Flux<EmployeeAggreegateID> sendfromEmployeeRepository(){
        log.info("%s -> WebClientService.sendfromEmployeeRepository -> %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now()));
        return  Flux.from(repositoryService.getRepo())
                    .flatMap(emp -> Mono.just(new CreateEmployeeRequestDTO(emp.getEmail(), emp.getAddress(), emp.getUserName())))
                    .flatMap(emp -> Mono.from(apiWebClient.createClient(emp).onErrorResume(ex -> {
                        throw new RuntimeException(ex);})))
                    .onErrorResume(e -> {
                        log.error("Error: " + e.getMessage());
                        return Flux.empty();
                    });} 
                    
    public Flux<CreateEmployeeClientRequestDTO> sendfromEmployeeClientRepository(){
                        log.info("%s -> WebClientService.sendfromEmployeeClientRepository -> %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now()));
                        return Flux.from(repositoryService.getRepo())
                                    .doOnNext(emp -> log.info("First! %s: ".formatted(emp.getEmail())))
                                    .publishOn(Schedulers.single())
                                    .flatMap(emp -> Mono.just(new CreateEmployeeRequestDTO(emp.getEmail(), emp.getAddress(), emp.getUserName())))
                                    .doOnNext(emp -> log.info("Employee _ 1 _: %s %s %s".formatted(emp.email(), emp.address(), emp.userName())))
                                    .publishOn(Schedulers.single())
                                    .doOnNext(emp -> log.info("Employee: %s %s %s".formatted(emp.email(), emp.address(), emp.userName())))
                                    .publishOn(Schedulers.single())
                                    .flatMap(emp -> Mono.from(apiWebClient.createClient(emp))
                                                    .publishOn(Schedulers.boundedElastic())
                                                    .flatMap(id -> Mono.just(new CreateEmployeeClientRequestDTO(id.id(), emp.email(), emp.address(), emp.userName()))))
                                    .onErrorResume(e -> {
                                        log.error("Error: " + e.getMessage());
                                        return Flux.empty();
                                    });
    }

}
