package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByAppointmentIdAndReviewerId(Long appointmentId, Long userId);

    List<Review> findByReviewerId(Long userId);

    List<Review> findByGroomerId(Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM reviews WHERE groomer_id = :groomerId AND rating = 4 OR rating = 5")
    List<Review> findByGroomerIdWhereRatingIsFourOrFive(@Param("groomerId")Long groomerId);


}
