package otus.springwebflux.webfluxclient.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = " Employee not create ")
public class EmployeeCreateInEventSourcingRuntimeException extends RuntimeException {
    public EmployeeCreateInEventSourcingRuntimeException() {
    }

    public EmployeeCreateInEventSourcingRuntimeException(String userName, Throwable ex) {
        super("WebClientService runtime exception.  EmployeeName: %s".formatted(userName), ex);
    }
}
