package com.bookitapp.Book.It.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class DataSourceConfig {

    @Autowired
    private LocalDatasourceProperties localDatasourceProperties;

    @Bean
    public DataSource herokuDataSource() {
        String dbURL = "jdbc:" + System.getenv("CLEARDB_GRAY_URL");
        String dbUsername = System.getenv("PROD_DB_USERNAME");
        String dbPassword = System.getenv("PROD_DB_PASSWORD");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dbURL);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Profile("local")
    @Bean
    public DataSource localDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(localDatasourceProperties.getUrl());
        dataSource.setUsername(localDatasourceProperties.getUsername());
        dataSource.setPassword(localDatasourceProperties.getPassword());

        return dataSource;
    }
}
