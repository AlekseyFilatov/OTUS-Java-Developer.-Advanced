package otus.springwebflux.webfluxclient;

import java.util.concurrent.TimeoutException;

import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import otus.springwebflux.webfluxclient.model.Employee;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebfluxclientSampleTests.class})
@TestConfiguration
class WebfluxclientSampleTests {

    /*
     * https://stackoverflow.com/questions/50329817/unable-to-start-reactivewebapplicationcontext-due-to-missing-reactivewebserverfa
     * https://stackoverflow.com/questions/54619839/test-unit-spring-boot-unable-to-register-mock-bean
     */
    //@MockBean
    //@Qualifier("WebClientSampleTests")
    final WebClient clientMock = WebClient.builder().baseUrl("http://localhost:8089").build();

    @Test
    void testFindEmployeesJson() throws TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        var employees = clientMock.get().uri("/employees/json").retrieve().bodyToFlux(Employee.class);
        employees.subscribe(employee -> {
            waiter.assertNotNull(employee);
            log.info("Client subscribes: {}", employee);
            waiter.resume();
        });
        waiter.await(3000, 9);
    }

    @Test
    void testFindEmployesStream() throws TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Flux<Employee> employees = clientMock.get().uri("/employees/stream").retrieve().bodyToFlux(Employee.class);
        employees.subscribe(employee -> {
            log.info("Client subscribes: {}", employee);
            waiter.assertNotNull(employee);
            waiter.resume();
        });
        waiter.await(3000, 9);
    }

    @Test
    void testFindEmployeesStreamBackPressure() throws TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Flux<Employee> employees = clientMock.get().uri("/employees/stream/back-pressure").retrieve().bodyToFlux(Employee.class);
        employees.map(this::doSomeSlowWork).subscribe(employee -> {
            waiter.assertNotNull(employee);
            log.info("Client subscribes: {}", employee);
            waiter.resume();
        });
        waiter.await(3000, 9);
    }

    private Employee doSomeSlowWork(Employee employee) {
        try {
            Thread.sleep(90);
        }
        catch (InterruptedException e) { }
        return employee;
    }
}
