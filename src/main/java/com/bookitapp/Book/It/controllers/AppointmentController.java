package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Dog;
import com.bookitapp.Book.It.models.Groomer;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.DogRepository;
import com.bookitapp.Book.It.repositories.GroomerRepository;
import com.bookitapp.Book.It.repositories.UserRepository;
import com.bookitapp.Book.It.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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
    private final DogRepository dogRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Autowired
    public AppointmentController(
            GroomerRepository groomerRepo,
            AppointmentRepository appointmentRepo,
            UserRepository userRepo,
            DogRepository dogRepo,
            PasswordEncoder passwordEncoder,
            AuthService authService) {
        this.groomerRepo = groomerRepo;
        this.appointmentRepo = appointmentRepo;
        this.userRepo = userRepo;
        this.dogRepo =dogRepo;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @GetMapping("/{appointmentId}")
    public String singleAppointmentPage(
            Model model,
            @PathVariable("appointmentId") Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUserObject = userRepo.findById(loggedInUser.getId()).get();
        model.addAttribute("user", loggedInUserObject);
        model.addAttribute("appointment", appointment);
        return "appointments/appointment";
    }

    @PostMapping
    public String createAppointment(
            @RequestParam("groomer-id") Long groomerId,
            @RequestParam("selected-date") String dateInput,
            @RequestParam("selected-time") String timeInput,
            @RequestParam("change-appointment-id") @Nullable Long changeAppointmentId,
            @RequestParam("dog-id") Long dogId) {
        boolean userIsAuthenticated = authService.isLoggedIn();

        if (userIsAuthenticated) {

            Appointment newAppointment = new Appointment();
            Groomer selectedGroomer = groomerRepo.findById(groomerId).get();
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User loggedInUserWithCurrentProps = userRepo.findById(loggedInUser.getId()).get();
            Dog dog = dogRepo.findById(dogId).get();

            String dateTimeString = dateInput + "T" + timeInput;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
//            ZonedDateTime utcDateTime = dateTime.atZone(ZoneId.of("UTC"));

//            dateTime = dateTime.minusHours(5);
            Long newAppointmentId = 0L;

            if (changeAppointmentId != null) {
                Appointment appointmentToChange = appointmentRepo.findById(changeAppointmentId).get();
                appointmentToChange.setAppointmentTime(dateTime);
                appointmentToChange.setGroomer(selectedGroomer);
                appointmentToChange.setDog(dog);
                appointmentRepo.save(appointmentToChange);
                return "redirect:/appointments/" + changeAppointmentId;
            } else {
                newAppointment.setGroomer(selectedGroomer);
                newAppointment.setUser(loggedInUserWithCurrentProps);
                newAppointment.setAppointmentTime(dateTime);
                newAppointment.setDog(dog);
                appointmentRepo.save(newAppointment);
                newAppointmentId = appointmentRepo.findByAppointmentTimeAndGroomer(dateTime, selectedGroomer).getId();
            }

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
