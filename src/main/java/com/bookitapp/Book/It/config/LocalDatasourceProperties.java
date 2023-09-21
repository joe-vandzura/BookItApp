package com.bookitapp.Book.It.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "local.datasource")
@Getter
@Setter
public class LocalDatasourceProperties {
    private String url;
    private String username;
    private String password;

}
