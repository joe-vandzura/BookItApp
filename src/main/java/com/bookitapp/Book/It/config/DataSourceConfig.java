package com.bookitapp.Book.It.config;

import com.bookitapp.Book.It.DatabaseKeys;
import lombok.AllArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class DataSourceConfig {

    private DatabaseKeys databaseKeys;

    @Bean
    public DataSource dataSource() {
        System.out.println("HERE");

        String dbURL;
        String dbUsername;
        String dbPassword;

        if (System.getenv("CLEARDB_GRAY_URL") == null) {
            dbURL = databaseKeys.databaseURL;
        } else {
            dbURL = System.getenv("CLEARDB_GRAY_URL");
        }

        if (System.getenv("PROD_DB_USERNAME") == null) {
            dbUsername = databaseKeys.databaseUsername;
        } else {
            dbUsername = System.getenv("PROD_DB_USERNAME");
        }

        if (System.getenv("PROD_DB_PASSWORD") == null) {
            dbPassword = databaseKeys.databasePassword;
        } else {
            dbPassword = System.getenv("PROD_DB_PASSWORD");
        }


        return DataSourceBuilder
                .create()
                .url(dbURL)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}
