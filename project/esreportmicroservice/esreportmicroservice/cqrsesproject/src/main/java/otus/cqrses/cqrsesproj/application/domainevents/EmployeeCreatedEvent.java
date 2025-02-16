package otus.cqrses.cqrsesproj.application.domainevents;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeAggregate;

@Data
@EqualsAndHashCode(callSuper=false)
public class EmployeeCreatedEvent extends BaseEvent {
    public static final String EMPLOYEE_CREATED_V1 = "EMPLOYEE_CREATED_V1";
    public static final String AGGREGATE_TYPE = EmployeeAggregate.AGGREGATE_TYPE;

    @Builder
    public EmployeeCreatedEvent(String aggregateId, String email, String userName, String address) {
        super(aggregateId);
        this.email = email;
        this.userName = userName;
        this.address = address;
    }

    private String email;
    private String userName;
    private String address;
}
