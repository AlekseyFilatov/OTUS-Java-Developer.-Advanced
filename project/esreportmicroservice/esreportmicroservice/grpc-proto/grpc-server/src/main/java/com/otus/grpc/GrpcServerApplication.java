package com.otus.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpcServerApplication {

	/*
	 * https://www.baeldung.com/spring-boot-grpc
	 * https://huongdanjava.com/introduction-to-grpc.html
	 * https://huongdanjava.com/generate-java-code-for-service-contract-in-grpc-using-protocol-buffers-maven-plugin.html
	 * https://huongdanjava.com/grpc-server-with-spring-boot-using-grpc-server-spring-boot-starter.html
	 * https://github.com/grpc-ecosystem/grpc-spring/tree/master/examples
	 * https://apidog.com/articles/how-to-send-grpc-request-with-postman/
	 */
	public static void main(String[] args) {
		SpringApplication.run(GrpcServerApplication.class, args);
	}

}
