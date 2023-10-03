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

    List<Appointment> findByUserIdOrderByAppointmentTimeAsc(Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM appointments WHERE user_id = :userId AND time > :currentDate ORDER BY time ASC")
    List<Appointment> getFutureAppointmentsByUserId(@Param("userId") Long id, @Param("currentDate") ZonedDateTime currentDate);

    @Query(nativeQuery = true, value = "SELECT * FROM appointments WHERE user_id = :userId AND time < :currentDate ORDER BY time ASC")
    List<Appointment> getPastAppointmentsByUserId(@Param("userId") Long id, @Param("currentDate") ZonedDateTime currentDate);

    @Query(nativeQuery = true, value = "SELECT * FROM appointments WHERE time >= NOW() AND time <= DATE_ADD(NOW(), INTERVAL 36 HOUR);")
    List<Appointment> findUpcomingAppointments();

    @Query(nativeQuery = true, value = "SELECT * FROM appointments WHERE time >= NOW() LIMIT 1;")
    Appointment findAFutureAppointment();

}
