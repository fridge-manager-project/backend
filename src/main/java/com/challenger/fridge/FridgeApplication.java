package com.challenger.fridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.challenger\\.fridge\\.util\\..*"))
public class FridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FridgeApplication.class, args);
	}

}
