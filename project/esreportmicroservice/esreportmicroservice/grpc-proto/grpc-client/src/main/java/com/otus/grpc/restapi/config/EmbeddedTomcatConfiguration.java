package com.otus.grpc.restapi.config;

import java.util.Arrays;

import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedTomcatConfiguration {

    @Value("${server.additional-ports}")
    private String additionalPorts;

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector[] additionalConnectors = additionalConnector();
        if(additionalConnectors != null && additionalConnectors.length > 0) {
            tomcat.addAdditionalTomcatConnectors(additionalConnectors);
        }
        return tomcat;
    }

    private Connector[] additionalConnector() {
        if(StringUtils.isNotBlank(additionalPorts)) {
            return Arrays.stream(additionalPorts.split(","))
                .map(String::trim)
                .map(p -> {
                    Connector connector = new Connector(Http11NioProtocol.class.getCanonicalName());
                    connector.setScheme("http");
                    connector.setPort(Integer.valueOf(p));
                    return connector;
                })
                .toArray(Connector[]::new);
        }
        return new Connector[0];
    }
}