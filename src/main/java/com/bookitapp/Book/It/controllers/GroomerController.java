package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/groomers")
@RequiredArgsConstructor
public class GroomerController {

    private final AppointmentRepository appointmentRepo;

    @GetMapping
    ResponseBody getGroomerDetails() {
        return null;
    }

    @GetMapping(value = "/getGroomerAvailability/{groomerId}", produces = "application/json")
    @ResponseBody
    public List<LocalDateTime> getGroomerAvailability(@PathVariable("groomerId") Long groomerId) {
        List<LocalDateTime> availabletimes = null;
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime dateOfEndOfWeek = currentDate.plusDays(7);

        List<Appointment> appointmentsForGroomerBetweenForTheWeek = appointmentRepo.findAppointmentsForGroomerForTheWeek(groomerId, currentDate, dateOfEndOfWeek);



        return availabletimes;
    }

}
