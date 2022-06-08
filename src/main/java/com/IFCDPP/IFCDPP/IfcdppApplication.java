package com.ifcdpp.ifcdpp;

import com.ifcdpp.ifcdpp.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
@EnableScheduling
public class IfcdppApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfcdppApplication.class, args);
	}
}

