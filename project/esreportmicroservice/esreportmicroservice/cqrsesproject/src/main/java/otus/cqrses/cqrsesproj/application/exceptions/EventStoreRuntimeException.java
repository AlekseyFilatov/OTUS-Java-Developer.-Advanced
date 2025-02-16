package otus.cqrses.cqrsesproj.application.exceptions;

public class EventStoreRuntimeException extends RuntimeException {
    public EventStoreRuntimeException() {
    }

    public EventStoreRuntimeException(String aggregateId, Exception e) {
        super("(EventStore)aggregateId runtime exception: " + aggregateId, e);
    }
}
