package com.bookitapp.Book.It;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DatabaseKeys {

    @Value("${DB_URL}")
    public String databaseURL;

    @Value("${DB_USERNAME}")
    public String databaseUsername;

    @Value("${DB_PASSWORD}")
    public String databasePassword;

}

