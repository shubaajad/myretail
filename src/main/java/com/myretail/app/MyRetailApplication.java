package com.myretail.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories({"com.myretail.dao"})
@ComponentScan("com.myretail")
public class MyRetailApplication {

	private static final Logger logger = LoggerFactory.getLogger(MyRetailApplication.class);
	
	public static void main(String[] args) {
		logger.trace("Initiatiling application");
		System.getProperties().put( "server.port", 8181 );
		SpringApplication.run(MyRetailApplication.class, args);
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MyRetailApplication.class);
    }
}
