package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Dog;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.DogRepository;
import com.bookitapp.Book.It.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/my-profile")
@RequiredArgsConstructor
public class MyProfileController {

    private final AppointmentRepository appointmentRepo;
    private final DogRepository dogRepo;

    @GetMapping("/appointments")
    public String myAppointmentsPage(Model model) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Appointment> appointments = appointmentRepo.findByUserIdOrderByAppointmentTimeAsc(loggedInUser.getId());

            model.addAttribute("appointments", appointments);
            return "profile/appointments";
        } else {
            return "redirect:/login";
        }

    }

    @GetMapping("/account")
    public String accountPage(Model model) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("dog", new Dog());
            List<Dog> dogs = dogRepo.findByOwnerId(loggedInUser.getId());

            model.addAttribute("dogs", dogs);
        } else {
            return "redirect:/login";
        }
        return "profile/account";
    }

    @PostMapping("/dogs/{dogId}")
    public String redirectToChangeDogMapping(
            @PathVariable("dogId") Long dogId,
            @RequestParam("name") String name,
            @RequestParam("breed") String breed,
            @RequestParam("age") int age,
            @RequestParam("sex") char sex,
            @RequestParam("rabiesVaccinationStatus") boolean rabiesVaccinationStatus
    ) {
        return "redirect:/dogs/" + dogId +  "?name=" + name + "?breed=" + breed + "?age=" + age + "?sex=" + sex + "?rabiesVaccinationStatus=" + rabiesVaccinationStatus;
    }

}
