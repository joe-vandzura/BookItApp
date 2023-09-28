package com.bookitapp.Book.It;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;

@SpringBootApplication(exclude = MailSenderAutoConfiguration.class)
public class BookItApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookItApplication.class, args);
	}

}
