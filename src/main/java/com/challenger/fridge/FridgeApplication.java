package com.challenger.fridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FridgeApplication {

	public static void main(String[] args) {
		System.out.println("\"master\" = " + "master2");
		SpringApplication.run(FridgeApplication.class, args);
	}

}
