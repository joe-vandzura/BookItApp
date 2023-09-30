package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByToken(String tokenString);

}
