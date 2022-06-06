package com.ifcdpp.ifcdpp;

import com.ifcdpp.ifcdpp.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class IfcdppApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfcdppApplication.class, args);
	}
}

