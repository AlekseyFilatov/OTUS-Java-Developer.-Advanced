package otus.cqrses.cqrsesproj.adapters.service;

import otus.cqrses.cqrsesproj.application.commands.ChangeAddressCommand;
import otus.cqrses.cqrsesproj.application.commands.ChangeEmailCommand;
import otus.cqrses.cqrsesproj.application.commands.CreateEmployeeCommand;

public interface EmployeeCommandService {

    String handle(CreateEmployeeCommand command);

    void handle(ChangeEmailCommand command);

    void handle(ChangeAddressCommand command);

    //void handle(ContactCommand command);
}
