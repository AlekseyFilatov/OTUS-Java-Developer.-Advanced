package otus.cqrses.cqrsesproj.adapters.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value(value = "${microservice.kafka.topics.employee-event-store:employee-event-store}")
    private String employeeTopicName;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic employeeEventStoreTopicInitializer(KafkaAdmin kafkaAdmin) {
        try {
            final var topic = new NewTopic(employeeTopicName, 3, (short) 1);
            kafkaAdmin.createOrModifyTopics(topic);
            log.info("(employeeEventStoreTopicInitializer) topic: {}", topic);
            return topic;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}