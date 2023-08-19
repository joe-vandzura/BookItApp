package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.GroomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final GroomerRepository groomerRepo;
    private final AppointmentRepository appointmentRepo;

    @PostMapping
    public String createAppointment(@RequestParam("groomer-id") Long groomerId, @RequestParam("selected-date-input") String dateInput, @RequestParam("selected-time-input") String timeInput) {
        System.out.println("Made it to createAppointment post mapping");
        Appointment newAppointment = new Appointment();
        newAppointment.setGroomer(groomerRepo.findById(groomerId).get());
        LocalDateTime dateTime = LocalDateTime.parse(dateInput + " " + timeInput);
        newAppointment.setAppointmentTime(dateTime);
        appointmentRepo.save(newAppointment);
        return "appointment/confirm";
    }

}
