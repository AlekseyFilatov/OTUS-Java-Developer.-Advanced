package otus.cqrses.cqrsesproj.application.commands;

public record CreateEmployeeCommand(String aggregateID, String email, String userName, String address) {
}
