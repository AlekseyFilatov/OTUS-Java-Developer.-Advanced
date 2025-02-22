package otus.cqrses.cqrsesproj.application.commands;

public record ChangeEmailCommand(String aggregateID, String newEmail) {
}