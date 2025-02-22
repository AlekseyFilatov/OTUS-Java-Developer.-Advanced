package otus.springwebflux.webfluxclient.exceptions;

public class EmployeeCreateInEventSourcingRuntimeException extends RuntimeException {
    public EmployeeCreateInEventSourcingRuntimeException() {
    }

    public EmployeeCreateInEventSourcingRuntimeException(String userName, Throwable ex) {
        super("WebClientService runtime exception.  EmployeeName: %s".formatted(userName), ex);
    }
}
