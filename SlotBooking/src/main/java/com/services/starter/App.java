package com.services.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Starter Class
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.services")
public class App {
    public static void main( String[] args )  {
    	String webPort = System.getenv("PORT");
	if (webPort == null || webPort.isEmpty()) {
		webPort = "5050";
	}
	System.setProperty("server.port", webPort);

	SpringApplication.run(App.class, args);
    }
}
