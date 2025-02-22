package otus.cqrses.cqrsesproj.application.exceptions;

import java.time.LocalDateTime;

public record ExceptionResponseDTO(int Status, String message, LocalDateTime timestamp) {
}