package com.otus.docum.reportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@SpringBootApplication
public class ReportserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportserviceApplication.class, args);
	}

}
