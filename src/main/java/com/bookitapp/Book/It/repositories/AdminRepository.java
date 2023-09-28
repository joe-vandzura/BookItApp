package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
