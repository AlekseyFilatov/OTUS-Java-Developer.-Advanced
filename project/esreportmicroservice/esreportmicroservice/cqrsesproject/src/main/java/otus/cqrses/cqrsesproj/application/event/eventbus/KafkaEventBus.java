package otus.cqrses.cqrsesproj.application.event.eventbus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import otus.cqrses.cqrsesproj.application.event.Event;
import otus.cqrses.cqrsesproj.application.event.EventBus;
import otus.cqrses.cqrsesproj.application.event.SerializerUtils;
import otus.cqrses.cqrsesproj.application.exceptions.KafkaEventBusException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventBus implements EventBus {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final static long sendTimeout = 3000;

    @Value(value = "${order.kafka.topics.employee-event-store:employee-event-store}")
    private String employeeTopicName;

    @Override
    @NewSpan
    public void publish(@SpanTag("events") List<Event> events) {
        final byte[] eventsBytes = SerializerUtils.serializeToJsonBytes(events.toArray(new Event[]{}));
        final ProducerRecord<String, byte[]> record = new ProducerRecord<>(employeeTopicName, eventsBytes);

        try {
            kafkaTemplate.send(record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publishing kafka record value >>>>> {}", new String(record.value()));

        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new KafkaEventBusException(ex.getMessage());
        } 
    }
}
