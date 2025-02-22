package otus.cqrses.cqrsesproj.application.event;

import java.util.List;

public interface EventBus {
    void publish(List<Event> events);
}
