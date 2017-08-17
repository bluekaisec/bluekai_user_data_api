package com.oracle.bluekai.sec.userdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class BkUserDataApiApplication {

	public static void main(String[] args) {

		try {
			SpringApplication.run(BkUserDataApiApplication.class, args);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
