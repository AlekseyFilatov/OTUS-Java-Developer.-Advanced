package otus.cqrses.cqrsesproj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@SpringBootApplication
public class CqrsesprojApplication {

	public static void main(String[] args) {
		SpringApplication.run(CqrsesprojApplication.class, args);
	}

}
