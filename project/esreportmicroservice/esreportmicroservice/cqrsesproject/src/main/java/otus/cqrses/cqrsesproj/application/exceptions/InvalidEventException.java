package otus.cqrses.cqrsesproj.application.exceptions;

public class InvalidEventException extends RuntimeException{
    public InvalidEventException() {
    }

    public InvalidEventException(String message) {
        super("invalid event: " + message);
    }
}
