package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Groomer;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.GroomerRepository;
import com.bookitapp.Book.It.repositories.UserRepository;
import com.bookitapp.Book.It.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Autowired
    public AppointmentController(
            GroomerRepository groomerRepo,
            AppointmentRepository appointmentRepo,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder,
            AuthService authService) {
        this.groomerRepo = groomerRepo;
        this.appointmentRepo = appointmentRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
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
        boolean userIsAuthenticated = authService.isLoggedIn();

        if (userIsAuthenticated) {

            Appointment newAppointment = new Appointment();
            Groomer selectedGroomer = groomerRepo.findById(groomerId).get();
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User loggedInUserWithCurrentProps = userRepo.findById(loggedInUser.getId()).get();
            newAppointment.setGroomer(selectedGroomer);
            newAppointment.setUser(loggedInUserWithCurrentProps);

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
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/{appointmentId}")
    public String changeAppointment(
            @PathVariable("appointmentId") Long appointmentId,
            @RequestParam("_method") String method) {

        if ("DELETE".equals(method)) {
            appointmentRepo.deleteById(appointmentId);
            return "redirect:/my-profile/appointments";
        }

        return "redirect:/my-profile/appointments";
    }

}
