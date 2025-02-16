package otus.cqrses.cqrsesproj.adapters.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import otus.cqrses.cqrsesproj.entities.domain.EmployeeDocument;

import java.util.Optional;

public interface EmployeeMongoRepository extends MongoRepository<EmployeeDocument, String> {

    Optional<EmployeeDocument> findByAggregateId(String aggregateId);

    void deleteByAggregateId(String aggregateId);
}