package otus.cqrses.cqrsesproj.application.exceptions;

public class BaseEventRuntimeException extends RuntimeException {
    public BaseEventRuntimeException() {
    }

    public BaseEventRuntimeException(String errMessage) {
        super("(EventStore)aggregateId runtime exception: " + errMessage);
    }
}