package otus.cqrses.cqrsesproj.application.event.eventbus;

import otus.cqrses.cqrsesproj.adapters.repository.EmployeeMongoRepository;
import otus.cqrses.cqrsesproj.application.domainevents.AddressUpdatedEvent;
import otus.cqrses.cqrsesproj.application.domainevents.EmailChangedEvent;
import otus.cqrses.cqrsesproj.application.domainevents.EmployeeCreatedEvent;
import otus.cqrses.cqrsesproj.application.event.Event;
import otus.cqrses.cqrsesproj.application.event.EventStoreDB;
import otus.cqrses.cqrsesproj.application.event.Projection;
import otus.cqrses.cqrsesproj.application.event.SerializerUtils;
import otus.cqrses.cqrsesproj.application.exceptions.EmployeeDocumentNotFoundException;
import otus.cqrses.cqrsesproj.application.mappers.EmployeeMapper;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeAggregate;
import otus.cqrses.cqrsesproj.entities.domain.EmployeeDocument;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeMongoProjection implements Projection {

    private final EmployeeMongoRepository mongoRepository;
    private final EventStoreDB eventStoreDB;
    private static final String SERVICE_NAME = "microservice";


    @KafkaListener(topics = {"${microservice.kafka.topics.employee-event-store}"},
            groupId = "${microservice.kafka.groupId}",
            concurrency = "${microservice.kafka.default-concurrency}")
    public void employeeMongoProjectionListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(EmployeeMongoProjection) topic: {}, offset: {}, partition: {}, timestamp: {}, data: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), new String(data));

        try {
            final Event[] events = SerializerUtils.deserializeEventsFromJsonBytes(data);
            this.processEvents(Arrays.stream(events).toList());
            ack.acknowledge();
            log.info("ack events: {}", Arrays.toString(events));
        } catch (Exception ex) {
            ack.nack(100, null);
            log.error("(EmployeeMongoProjection) topic: {}, offset: {}, partition: {}, timestamp: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), ex);
        }
    }

    @NewSpan
    private void processEvents(@SpanTag("events") List<Event> events) {
        if (events.isEmpty()) return;

        try {
            events.forEach(this::when);
        } catch (Exception ex) {
            mongoRepository.deleteByAggregateId(events.get(0).getAggregateId());
            final var aggregate = eventStoreDB.load(events.get(0).getAggregateId(), EmployeeAggregate.class);
            final var document = EmployeeMapper.employeeDocumentFromAggregate(aggregate);
            final var result = mongoRepository.save(document);
            log.info("(processEvents) saved document: {}", result);
        }
    }

    @Override
    @NewSpan
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public void when(@SpanTag("event") Event event) {
        final var aggregateId = event.getAggregateId();
        log.info("(when) >>>>> aggregateId: {}", aggregateId);

        switch (event.getEventType()) {
            case EmployeeCreatedEvent.EMPLOYEE_CREATED_V1 ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), EmployeeCreatedEvent.class));
            case EmailChangedEvent.EMAIL_CHANGED_V1 ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), EmailChangedEvent.class));
            case AddressUpdatedEvent.ADDRESS_UPDATED_V1 ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), AddressUpdatedEvent.class));
           default -> log.error("unknown event type: {}", event.getEventType());
        }
    }


    @NewSpan
    private void handle(@SpanTag("event") EmployeeCreatedEvent event) {
        log.info("(when) EmployeeCreatedEvent: {}, aggregateID: {}", event, event.getAggregateId());

        final var document = EmployeeDocument.builder()
                .aggregateId(event.getAggregateId())
                .email(event.getEmail())
                .address(event.getAddress())
                .userName(event.getUserName())
                .build();

        final var insert = mongoRepository.insert(document);
        log.info("(EmployeeCreatedEvent) insert: {}", insert);
    }

    @NewSpan
    private void handle(@SpanTag("event") EmailChangedEvent event) {
        log.info("(when) EmailChangedEvent: {}, aggregateID: {}", event, event.getAggregateId());
        final var documentOptional = mongoRepository.findByAggregateId(event.getAggregateId());
        if (documentOptional.isEmpty())
            throw new EmployeeDocumentNotFoundException(event.getAggregateId());

        final var document = documentOptional.get();
        document.setEmail(event.getNewEmail());
        mongoRepository.save(document);
    }

    @NewSpan
    private void handle(@SpanTag("event") AddressUpdatedEvent event) {
        log.info("(when) AddressUpdatedEvent: {}, aggregateID: {}", event, event.getAggregateId());
        final var documentOptional = mongoRepository.findByAggregateId(event.getAggregateId());
        if (documentOptional.isEmpty())
            throw new EmployeeDocumentNotFoundException(event.getAggregateId());

        final var document = documentOptional.get();
        document.setAddress(event.getNewAddress());
        mongoRepository.save(document);
    }

}
