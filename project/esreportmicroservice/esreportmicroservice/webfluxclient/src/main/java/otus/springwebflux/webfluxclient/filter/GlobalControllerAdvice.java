package otus.springwebflux.webfluxclient.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import otus.springwebflux.webfluxclient.exceptions.EmployeeCreateInEventSourcingRuntimeException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import org.springframework.web.ErrorResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;




@ControllerAdvice
@Slf4j
@RequiredArgsConstructor

@Order(2)
public class GlobalControllerAdvice  {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmployeeCreateInEventSourcingRuntimeException.class)
    public ErrorResponse employeeCreateInEventSourcingRuntimeException(EmployeeCreateInEventSourcingRuntimeException ex) {
        return ErrorResponse.builder(ex, HttpStatusCode.valueOf(404), ex.getCause().getMessage())
                            .build();
    }
}
