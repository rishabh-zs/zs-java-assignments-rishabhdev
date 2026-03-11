package com.zs.assignment11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(Initialize.class)
public class DemoAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoAppApplication.class, args);
	}
}
