package com.bookitapp.Book.It;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = MailSenderAutoConfiguration.class)
@EnableScheduling
public class BookItApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookItApplication.class, args);
	}

}
