package otus.cqrses.cqrsesproj.entities.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Employee {

	private @Id @GeneratedValue Long employeeId;
	private String userName;
	private String password;
    private String email;
    private String address;

}
