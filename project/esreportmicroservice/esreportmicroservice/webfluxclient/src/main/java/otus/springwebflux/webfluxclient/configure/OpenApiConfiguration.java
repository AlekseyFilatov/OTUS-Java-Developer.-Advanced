package otus.springwebflux.webfluxclient.configure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.stereotype.Component;


@OpenAPIDefinition(info = @Info(title = "WebClient for Event Sourcing Microservice",
        description = "WebClient for Event Sourcing Microservice",
        contact = @Contact(name = "Aleksey Filatov", email = "???", url = "???")))
@Component
public class OpenApiConfiguration {
}
