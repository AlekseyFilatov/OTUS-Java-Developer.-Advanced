package otus.cqrses.cqrsesproj.application.exceptions;

public record InternalServerErrorResponse(int status, String message, String timestamp) {
}
