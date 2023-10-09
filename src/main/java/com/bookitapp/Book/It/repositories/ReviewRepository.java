package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
