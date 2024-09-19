package com.rakbank.userservice;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.rakbank.userservice.service.PasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new PasswordEncoder(BCrypt.Version.VERSION_2A);
	}

}
