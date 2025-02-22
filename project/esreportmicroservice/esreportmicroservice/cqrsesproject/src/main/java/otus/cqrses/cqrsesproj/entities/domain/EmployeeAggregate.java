package otus.cqrses.cqrsesproj.entities.domain;

import otus.cqrses.cqrsesproj.application.domainevents.AddressUpdatedEvent;
import otus.cqrses.cqrsesproj.application.domainevents.EmailChangedEvent;
import otus.cqrses.cqrsesproj.application.domainevents.EmployeeCreatedEvent;
import otus.cqrses.cqrsesproj.application.event.AggregateRoot;
import otus.cqrses.cqrsesproj.application.event.Event;
import otus.cqrses.cqrsesproj.application.event.SerializerUtils;
import otus.cqrses.cqrsesproj.application.exceptions.InvalidAddressException;
import otus.cqrses.cqrsesproj.application.exceptions.InvalidEmailException;
import otus.cqrses.cqrsesproj.application.exceptions.InvalidEventTypeException;
import lombok.*;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmployeeAggregate extends AggregateRoot {


    public static final String AGGREGATE_TYPE = "EmployeeAggregate";

    public EmployeeAggregate(String id) {
        super(id, AGGREGATE_TYPE);
    }

    private String email;
    private String userName;
    private String address;
    //private BigDecimal balance;


    @Override
    public void when(Event event) {
        switch (event.getEventType()) {
            case EmployeeCreatedEvent.EMPLOYEE_CREATED_V1 ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), EmployeeCreatedEvent.class));
            case EmailChangedEvent.EMAIL_CHANGED_V1 ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), EmailChangedEvent.class));
            case AddressUpdatedEvent.ADDRESS_UPDATED_V1 ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), AddressUpdatedEvent.class));
            /*case BalanceDepositedEvent.BALANCE_DEPOSITED ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), BalanceDepositedEvent.class));*/
            default -> throw new InvalidEventTypeException(event.getEventType());
        }
    }

    private void handle(final EmployeeCreatedEvent event) {
        this.email = event.getEmail();
        this.userName = event.getUserName();
        this.address = event.getAddress();
        //this.balance = BigDecimal.valueOf(0);
    }

    private void handle(final EmailChangedEvent event) {
        Objects.requireNonNull(event.getNewEmail());
        if (event.getNewEmail().isBlank()) throw new InvalidEmailException();
        this.email = event.getNewEmail();
    }

    private void handle(final AddressUpdatedEvent event) {
        Objects.requireNonNull(event.getNewAddress());
        if (event.getNewAddress().isBlank()) throw new InvalidAddressException();
        this.address = event.getNewAddress();
    }

    /*private void handle(final BalanceDepositedEvent event) {
        Objects.requireNonNull(event.getAmount());
        this.balance = this.balance.add(event.getAmount());
    }*/

    public void createEmployee(String email, String address, String userName) {
        final var data = EmployeeCreatedEvent.builder()
                .aggregateId(id)
                .email(email)
                .address(address)
                .userName(userName)
                .build();

        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event = this.createEvent(EmployeeCreatedEvent.EMPLOYEE_CREATED_V1, dataBytes, null);
        this.apply(event);
    }

    public void changeEmail(String email) {
        final var data = EmailChangedEvent.builder().aggregateId(id).newEmail(email).build();
        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event = this.createEvent(EmailChangedEvent.EMAIL_CHANGED_V1, dataBytes, null);
        apply(event);

    }

    public void changeAddress(String newAddress) {
        final var data = AddressUpdatedEvent.builder().aggregateId(id).newAddress(newAddress).build();
        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event = this.createEvent(AddressUpdatedEvent.ADDRESS_UPDATED_V1, dataBytes, null);
        apply(event);
    }

    /*public void depositBalance(BigDecimal amount) {
        final var data = BalanceDepositedEvent.builder().aggregateId(id).amount(amount).build();
        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event = this.createEvent(BalanceDepositedEvent.BALANCE_DEPOSITED, dataBytes, null);
        apply(event);
    }*/


    @Override
    public String toString() {
        return "EmployeeAggregate{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
               // ", balance=" + balance +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", version=" + version +
                ", changes=" + changes.size() +
                '}';
    }
}