package com.LibraryManagementApplication.LibraryManagementApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.LibraryManagementApplication.LibraryManagementApplication.repositories")
public class Application
{

	public static void main(String[] args)

	{
		SpringApplication.run(Application.class, args);
	}

}
