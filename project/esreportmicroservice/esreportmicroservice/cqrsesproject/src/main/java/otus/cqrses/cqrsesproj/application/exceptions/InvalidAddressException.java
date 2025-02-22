package otus.cqrses.cqrsesproj.application.exceptions;

public class InvalidAddressException extends RuntimeException {
    public InvalidAddressException() {
        super();
    }

    public InvalidAddressException(String address) {
        super("(EmployeeAggregate)invalid address: " + address);
    }
}

