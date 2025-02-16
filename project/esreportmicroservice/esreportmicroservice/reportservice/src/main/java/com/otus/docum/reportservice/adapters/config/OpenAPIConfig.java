package com.otus.docum.reportservice.adapters.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Report Microservice", version = "1.0",
                license = @License(name = "Aleksey Filatov"),
                contact = @Contact(name = "Aleksey Filatov", email = "???", url = "???")),
        servers = {
                @Server(description = "Application available on localhost")
        } )
public class OpenAPIConfig {}