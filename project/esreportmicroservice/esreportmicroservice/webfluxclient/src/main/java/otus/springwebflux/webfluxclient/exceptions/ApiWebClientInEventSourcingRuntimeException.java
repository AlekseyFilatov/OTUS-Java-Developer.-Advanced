package otus.springwebflux.webfluxclient.exceptions;

public class ApiWebClientInEventSourcingRuntimeException extends RuntimeException {
    public ApiWebClientInEventSourcingRuntimeException() {
    }

    public ApiWebClientInEventSourcingRuntimeException(String error, Throwable ex) {
        super("apiWebClient runtime exception.  Error: %s".formatted(error), ex);
    }
}
