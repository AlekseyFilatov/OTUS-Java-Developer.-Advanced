package otus.springwebflux.webfluxclient.exceptions;

public class ApiWebClientException extends RuntimeException {
    public ApiWebClientException() {
    }

    public ApiWebClientException(String message) {
        super("(ApiWebClient)Error endpoint with status code:" + message);
    }
}

