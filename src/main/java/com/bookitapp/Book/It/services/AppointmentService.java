package com.bookitapp.Book.It.services;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;

    public List<Appointment> getUpocmingAppointments() {
        List<Appointment> upcomingAppointmnets = appointmentRepo.findUpcomingAppointments();
        return upcomingAppointmnets;
    }

}
