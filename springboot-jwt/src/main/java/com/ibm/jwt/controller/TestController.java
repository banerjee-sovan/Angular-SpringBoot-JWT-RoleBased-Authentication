package com.ibm.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/hello")
	public String sayHello() {
		return "Spring Boot JWT Backend Project is running!!!!";
	}
}
