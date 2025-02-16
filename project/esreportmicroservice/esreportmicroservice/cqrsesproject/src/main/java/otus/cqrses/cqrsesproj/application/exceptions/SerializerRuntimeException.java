package otus.cqrses.cqrsesproj.application.exceptions;

public class SerializerRuntimeException extends RuntimeException {
    public SerializerRuntimeException() {
    }

    public SerializerRuntimeException(String errorMessage, Exception e) {
        super("(SerializerUtils)serializer runtime exception: " + errorMessage, e);
    }
}

