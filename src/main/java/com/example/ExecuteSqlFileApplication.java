package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ExecuteSqlFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExecuteSqlFileApplication.class, args);
	}

}
