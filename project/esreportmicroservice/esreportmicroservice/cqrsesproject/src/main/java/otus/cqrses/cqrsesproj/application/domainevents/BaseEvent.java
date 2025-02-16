package otus.cqrses.cqrsesproj.application.domainevents;

import lombok.Data;
import lombok.NoArgsConstructor;
import otus.cqrses.cqrsesproj.application.exceptions.BaseEventRuntimeException;

import java.util.Objects;

@Data
@NoArgsConstructor
public abstract class BaseEvent {
    protected String aggregateId;

    protected BaseEvent(String aggregateId) {
        Objects.requireNonNull(aggregateId);
        if (aggregateId.isBlank()) throw new BaseEventRuntimeException("BaseEvent aggregateId is required");
        this.aggregateId = aggregateId;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "aggregateId='" + aggregateId + 
                '}';
    }
}
