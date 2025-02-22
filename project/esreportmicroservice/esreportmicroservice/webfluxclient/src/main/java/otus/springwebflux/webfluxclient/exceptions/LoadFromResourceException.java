package otus.springwebflux.webfluxclient.exceptions;

public class LoadFromResourceException  extends RuntimeException {
    public LoadFromResourceException() {
    }

    public LoadFromResourceException(String message) {
        super("(FileService)Error load from resource:" + message);
    }
}