package com.validator.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Boots up the ReST service.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.validator.trade"})
public class Application {

    /**
     * Starts the Application.
     * 
     * @param args the arguments passed
     */
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
