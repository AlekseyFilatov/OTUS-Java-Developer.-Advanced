package otus.cqrses.cqrsesproj.application.exceptions;

public class KafkaEventBusException extends RuntimeException {
    public KafkaEventBusException() {
    }

    public KafkaEventBusException(String ex) {
        super("(KafkaEventBus)send event runtime exception: " + ex);
    }
}
