package otus.springwebflux.webfluxclient.controller;

import java.time.Duration;

import java.util.stream.Stream;

import otus.springwebflux.webfluxclient.dto.CreateEmployeeClientRequestDTO;
import otus.springwebflux.webfluxclient.dto.CreateEmployeeRequestDTO;
import otus.springwebflux.webfluxclient.dto.EmployeeAggreegateID;
import otus.springwebflux.webfluxclient.model.Employee;

import otus.springwebflux.webfluxclient.repository.EmployeeRepositoryImpl;
import otus.springwebflux.webfluxclient.service.ApiWebClient;
import otus.springwebflux.webfluxclient.service.FileService;
import otus.springwebflux.webfluxclient.service.WebClientService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.core.io.buffer.DataBuffer;

import org.springframework.http.MediaType;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * https://habr.com/ru/articles/811289/
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final ThreadPoolTaskExecutor taskExecutor;
    private final ApiWebClient apiWebClient;
    private final WebClientService webClientService;

    @GetMapping(path = "/reportemployees", produces = "application/pdf") 
    public Mono<DataBuffer> reportEmployees() {
        return webClientService.reportEmployees();
    }       


    @GetMapping(path = "/fileemployees/{fileName}")
    public Flux<Employee> fileEmployeesRead(@PathVariable("fileName") String fileName) {
        return webClientService.loadFileToInMemRepository(fileName).doOnNext(msg -> log.info("%s -> WebClientService.loadFileToInMemRepository -> %s, File: %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now(), fileName)));
    }

    @GetMapping(path = "/repositoryEmployee")
    public Flux<EmployeeAggreegateID> sendEmployeeRepository() {
     return webClientService.sendfromEmployeeRepository().doOnNext(msg -> log.info("%s -> WebClientService.sendfromEmployeeRepository -> %s, Client: %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now(), msg)));
    }

    @PostMapping(path = "/employeees")
    public Flux<EmployeeAggreegateID> createEmployeeES(@RequestBody CreateEmployeeRequestDTO dto) {
        return Flux.from(webClientService.createClientES(dto)).doOnNext(msg -> log.info("%s -> WebClientService.sendfromEmployeeRepository -> %s, Client: %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now(), msg)));
    }

    @GetMapping("/employee")
    public Flux<EmployeeAggreegateID> createEmployee() {
        String fileName = "listEmpl.csv";
        webClientService.loadFileToInMemRepository(fileName)
            .doOnNext(msg -> log.info("%s -> webClientService.loadFileToInMemRepository -> %s, File: %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now(), fileName))).subscribe();
            //.flatMap(emp -> Mono.from(webClientService.sendfromEmployeeClientRepository()));
            //.flatMap(emp -> Flux.from(webClientService.createClientGRPC(emp)));
            
        return Flux.from(webClientService.sendfromEmployeeClientRepository())
                    .flatMap(emp -> Mono.from(webClientService.createClientGRPC(emp)));
    }

    @PostMapping(path = "/employeegrpc")
    public Flux<EmployeeAggreegateID> createEmployeeGRPC(@RequestBody CreateEmployeeClientRequestDTO dto) {
        return Flux.from(webClientService.createClientGRPC(dto)).doOnNext(msg -> log.info("%s -> WebClientService.sendfromEmployeeRepository -> %s, Client: %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now(), msg)));
    }

    @GetMapping("/json")
    public Flux<Employee> findEmployeesJson() {
        return Flux.fromStream(this::prepareStream)
                .doOnNext(employee -> log.info("Server produces: {}", employee));
    }

    @GetMapping(value = "/stream", produces = "application/stream+json")
    public Flux<Employee> findEmployeesStream() {
        return Flux.fromStream(this::prepareStream).delaySequence(Duration.ofMillis(100))
                .doOnNext(employee -> log.info("Server produces: {}", employee));
    }

    @GetMapping(value = "/stream/back-pressure", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Employee> findEmployeesStreamBackPressure() {
        return Flux.fromStream(this::prepareStream).delayElements(Duration.ofMillis(100))
                .doOnNext(employee -> log.info("Server produces: {}", employee));
    }

    private Stream<Employee> prepareStream() {
        return Stream.of(
            new Employee(Long.valueOf(1), "Name01_Samwise", "Surname01", "Name01.gmail.com", "Street 1 home 11"),
            new Employee(Long.valueOf(2), "Name02_Samwise", "Surname02", "Name02.gmail.com", "Street 2 home 12"),
            new Employee(Long.valueOf(3), "Name03_Samwise", "Surname03", "Name03.gmail.com", "Street 3 home 13"),
            new Employee(Long.valueOf(4), "Name04_Samwise", "Surname04", "Name04.gmail.com", "Street 4 home 14"),
            new Employee(Long.valueOf(5), "Name05_Samwise", "Surname05", "Name05.gmail.com", "Street 5 home 15"),
            new Employee(Long.valueOf(6), "Name06_Samwise", "Surname06", "Name06.gmail.com", "Street 6 home 16"),
            new Employee(Long.valueOf(7), "Name07_Samwise", "Surname07", "Name07.gmail.com", "Street 7 home 17"),
            new Employee(Long.valueOf(8), "Name08_Samwise", "Surname08", "Name08.gmail.com", "Street 8 home 18"),
            new Employee(Long.valueOf(9), "Name09_Samwise", "Surname09", "Name09.gmail.com", "Street 9 home 19")
        );
    }

    private Stream<Employee> prepareStreamPart1() {
        return Stream.of(
            new Employee(Long.valueOf(10), "Name10_Samwise", "Surname10", "Name11.gmail.com", "Street 11 home 11"),
            new Employee(Long.valueOf(11), "Name11_Samwise", "Surname11", "Name12.gmail.com", "Street 12 home 12"),
            new Employee(Long.valueOf(12), "Name12_Samwise", "Surname12", "Name13.gmail.com", "Street 13 home 13")
        );
    }

    @GetMapping("/integration/{param}")
    public Flux<Employee> findEmployeesIntegration(@PathVariable("param") String param) {

        return Flux.fromStream(this::prepareStreamPart1).log()
            .mergeWith(
                apiWebClient.webClient().get().uri("/slow/" + param)
                    .retrieve()
                    .bodyToFlux(Employee.class)
                    .log()
            );
    }

    @GetMapping("/integration-in-different-pool/{param}")
    public Flux<Employee> findEmployeesIntegrationInDifferentPool(@PathVariable("param") String param) {
        return Flux.fromStream(this::prepareStreamPart1).log()
            .mergeWith(                
                apiWebClient.webClient().get().uri("/slow/" + param)
                    .retrieve()
                    .bodyToFlux(Employee.class)
                    .log()
                    .publishOn(Schedulers.fromExecutor(taskExecutor))
            );
    }

}

