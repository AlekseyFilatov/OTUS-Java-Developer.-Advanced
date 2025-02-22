package otus.cqrses.cqrsesproj.application.exceptions;

public class InvalidEventTypeException extends RuntimeException {
    public InvalidEventTypeException() {
    }

    public InvalidEventTypeException(String eventType) {
        super("(EmployeeAggregate)invalid event type: " + eventType);
    }
}
