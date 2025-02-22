package otus.cqrses.cqrsesproj.application.domainevents;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeAggregate;

@Data
@EqualsAndHashCode(callSuper=false)
public class AddressUpdatedEvent extends BaseEvent {
    public static final String ADDRESS_UPDATED_V1 = "ADDRESS_UPDATED_V1";
    public static final String AGGREGATE_TYPE = EmployeeAggregate.AGGREGATE_TYPE;

    @Builder 
    public AddressUpdatedEvent(String aggregateId, String newAddress) {
        super(aggregateId);
        this.newAddress = newAddress;
    }

    private String newAddress;

    @Override
    public String toString() {
        return "AddressUpdatedEvent{" +
                "ADDRESS_UPDATED_V1='" + ADDRESS_UPDATED_V1 + 
                "AGGREGATE_TYPE='" + AGGREGATE_TYPE +
                '}';
    }
}