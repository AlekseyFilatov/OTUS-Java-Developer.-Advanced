package otus.cqrses.cqrsesproj.application.domainevents;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeAggregate;

@Data
@EqualsAndHashCode(callSuper=false)
public class EmailChangedEvent extends BaseEvent {
    public static final String EMAIL_CHANGED_V1 = "EMAIL_CHANGED_V1";
    public static final String AGGREGATE_TYPE = EmployeeAggregate.AGGREGATE_TYPE;

    private String newEmail;

    @Builder
    public EmailChangedEvent(String aggregateId, String newEmail) {
        super(aggregateId);
        this.newEmail = newEmail;
    }
}
