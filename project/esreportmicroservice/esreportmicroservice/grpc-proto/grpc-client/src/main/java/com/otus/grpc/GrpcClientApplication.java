package com.otus.grpc;

import com.otus.grpc.Service.ShopClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


//@ComponentScan(basePackages = "com.otus.grpc.restapi.controller.EmployeeController")
@SpringBootApplication
public class GrpcClientApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(GrpcClientApplication.class, args);
		ShopClient shopServer = applicationContext.getBean(ShopClient.class);
		shopServer.createUser("1", "BilboBaggins", "BilboBaggins@email.com");
		shopServer.changeUserEmail("1","burglarBilbo@email.com");
		shopServer.changeUserName("1","FrodoBaggins");
		shopServer.createProduct("99","Jokes");
		shopServer.addProductToCard("1","99");
	}
}