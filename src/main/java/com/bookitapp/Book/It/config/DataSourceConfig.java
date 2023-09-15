package com.bookitapp.Book.It.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        String dbUrl = System.getenv("CLEARDB_GRAY_URL");
        String dbUsername = System.getenv("PROD_DB_USERNAME");
        String dbPassword = System.getenv("PROD_DB_PASSWORD");

        return DataSourceBuilder
                .create()
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}
