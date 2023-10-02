package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    User findByUsername(String username);

    List<User> findByAppointments(Appointment appointment);

}
