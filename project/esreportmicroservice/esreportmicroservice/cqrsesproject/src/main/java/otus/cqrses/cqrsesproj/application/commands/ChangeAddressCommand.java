package otus.cqrses.cqrsesproj.application.commands;

public record ChangeAddressCommand(String aggregateID, String newAddress) {
}
