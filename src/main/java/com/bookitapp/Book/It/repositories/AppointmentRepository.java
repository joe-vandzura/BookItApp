package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Groomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM appointments WHERE groomer_id = :groomerId AND time > :currentDate AND time < :dateOfEndOfWeek")
    List<Appointment> findAppointmentsForGroomerForTheWeek(@Param("groomerId")Long groomerId, @Param("currentDate") ZonedDateTime currentDate, @Param("dateOfEndOfWeek") ZonedDateTime dateOfEndOfWeek);

    Appointment findByAppointmentTimeAndGroomer(LocalDateTime appointmentTime, Groomer groomer);

    List<Appointment> findByUserId(Long userId);
}
