package com.IFCDPP.IFCDPP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"controllers"})
@ComponentScan(basePackages={"models"})
@ComponentScan(basePackages={"repo"})
public class IfcdppApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfcdppApplication.class, args);
	}
}

