package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByAppointmentIdAndReviewerId(Long appointmentId, Long userId);

    List<Review> findByReviewerId(Long userId);

}
