package otus.cqrses.cqrsesproj.application.exceptions;

public class EmployeeDocumentNotFoundException extends RuntimeException {
    public EmployeeDocumentNotFoundException() {
    }

    public EmployeeDocumentNotFoundException(String id) {
        super("(EmployeeMongoProjection)Employee document not found id:" + id);
    }
}
