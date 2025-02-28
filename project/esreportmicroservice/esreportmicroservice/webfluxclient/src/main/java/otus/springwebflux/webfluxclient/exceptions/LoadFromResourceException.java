package otus.springwebflux.webfluxclient.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
public class LoadFromResourceException extends RuntimeException{
    public LoadFromResourceException() {
    }

    public LoadFromResourceException(String fileName, Exception e) {
        super(String.format("FileService runtime exception in : %s", fileName) , e);
    }
}