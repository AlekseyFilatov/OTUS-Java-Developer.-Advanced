package otus.cqrses.cqrsesproj.application.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super();
    }

    public InvalidEmailException(String email) {
        super("(EmployeeAggregate)invalid email address: " + email);
    }
}