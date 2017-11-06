package com.ibm.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.ibm.demo.repository")
public class MobileLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileLoginApplication.class, args);
	}
	
	public void run() {
	}
}
