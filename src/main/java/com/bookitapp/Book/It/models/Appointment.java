package com.bookitapp.Book.It.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time", nullable = false)
    private LocalDateTime appointmentTime;

    @ManyToOne
    @JoinColumn (name = "groomer_id")
    private Groomer groomer;

    public String toString() {
        return String.valueOf(appointmentTime);
    }

//    public List<LocalDateTime> compareAppointmentTimes(LocalDateTime timeToCompare, List<LocalDateTime> appointmentTimes) {
//
//    }

}
