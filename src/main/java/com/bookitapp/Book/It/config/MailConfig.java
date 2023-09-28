package com.bookitapp.Book.It.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
@AllArgsConstructor
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String host = System.getenv("PROD_EMAIL_HOST");
        int port = Integer.parseInt(System.getenv("PROD_EMAIL_PORT"));
        String username = System.getenv("PROD_EMAIL_USERNAME");
        String password = System.getenv("PROD_EMAIL_PASSWORD");

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

}
