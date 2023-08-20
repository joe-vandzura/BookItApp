package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    User findByUsername(String username);

}
