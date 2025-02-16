package otus.cqrses.cqrsesproj.application.exceptions;

public class AggregateNotFoundException extends RuntimeException {
    public AggregateNotFoundException() {
        super();
    }

    public AggregateNotFoundException(String aggregateID) {
        super("aggregate not found id:" + aggregateID);
    }
}