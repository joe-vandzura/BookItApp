package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Groomer;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.GroomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final GroomerRepository groomerRepo;
    private final AppointmentRepository appointmentRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppointmentController(
            GroomerRepository groomerRepo,
            AppointmentRepository appointmentRepo,
            PasswordEncoder passwordEncoder) {
        this.groomerRepo = groomerRepo;
        this.appointmentRepo = appointmentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{appointmentId}")
    public String singleAppointmentPage(
            Model model,
            @PathVariable("appointmentId") Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        model.addAttribute("appointment", appointment);
        return "appointments/appointment";
    }

    @PostMapping
    public String createAppointment(
            @RequestParam("groomer-id") Long groomerId,
            @RequestParam("selected-date") String dateInput,
            @RequestParam("selected-time") String timeInput) {

        System.out.println("Made it to createAppointment post mapping");

        Appointment newAppointment = new Appointment();
        Groomer selectedGroomer = groomerRepo.findById(groomerId).get();
        newAppointment.setGroomer(selectedGroomer);

        if (dateInput.trim().isEmpty() || timeInput.trim().isEmpty()) {
            System.out.println("dateInput and/pr timeInput is EMPTY!!!");
            return "redirect:/calendar";
        }

        String dateTimeString = dateInput + "T" + timeInput; // Combine date and time with 'T' separator
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"); // Define custom pattern
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter); // Parse with custom pattern
        dateTime = dateTime.minusHours(5);
        newAppointment.setAppointmentTime(dateTime);
        appointmentRepo.save(newAppointment);

        Long newAppointmentId = appointmentRepo.findByAppointmentTimeAndAndGroomer(dateTime, selectedGroomer).getId();
        return "redirect:/appointments/" + newAppointmentId;
    }

}
