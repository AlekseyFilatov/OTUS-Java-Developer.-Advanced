package otus.cqrses.cqrsesproj.entities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Employee")
public class EmployeeDocument {

    @BsonProperty(value = "_id")
    private String id;

    @BsonProperty(value = "aggregateId")
    private String aggregateId;

    @BsonProperty(value = "email")
    private String email;

    @BsonProperty(value = "userName")
    private String userName;

    @BsonProperty(value = "address")
    private String address;

}