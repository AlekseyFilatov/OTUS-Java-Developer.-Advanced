package otus.springwebflux.webfluxclient.service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import otus.springwebflux.webfluxclient.model.Employee;
import otus.springwebflux.webfluxclient.repository.EmployeeRepository;
import reactor.core.publisher.Flux;

@Slf4j
@Lazy
@RequiredArgsConstructor
@Service
public class RepositoryService {

    private final EmployeeRepository employeeRepository;

    public Flux<Employee> getRepo() {
        log.info("%s -> RepositoryService.getRepo -> %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now()));
        return Flux.fromIterable(employeeRepository.getRepo().values().stream().toList())
        .doOnNext(repo -> log.info("UserName: %s".formatted(repo.getUserName())));
    }

    public Flux<Employee> getRepoCollect() {
        log.info("%s -> RepositoryService.getRepo -> %s".formatted(Thread.currentThread().getName(),java.time.LocalTime.now()));
        return Flux.fromIterable(employeeRepository.getRepo().values().stream().collect(Collectors.toList()));
    }

}
