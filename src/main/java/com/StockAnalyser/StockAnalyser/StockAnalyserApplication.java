package com.StockAnalyser.StockAnalyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class StockAnalyserApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockAnalyserApplication.class, args);
	}

	@GetMapping("/")
	public String apiRoot(){
		return "Hello Oswin Chen Jui Tai!";

	}

}
