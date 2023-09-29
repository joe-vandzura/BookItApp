package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Dog;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.DogRepository;
import com.bookitapp.Book.It.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final DogRepository dogRepo;
    private final AppointmentRepository appointmentRepo;

    @GetMapping
    String calendarPageFromIndex(
            Model model,
            @RequestParam(name = "groomerId") @Nullable Long groomerId) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Dog> dogs = dogRepo.findByOwnerId(loggedInUser.getId());
            if (dogs.isEmpty()) {
                return "redirect:/my-profile/account?dogless";
            }
            model.addAttribute("dogs", dogs);
            if (groomerId != null) {
                model.addAttribute("groomerId", groomerId);
            }
            return "calendar";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/{changeAppointmentId}")
    String calendarPage(
            @PathVariable("changeAppointmentId") @Nullable Long changeAppointmentId,
            Model model) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Dog> dogs = dogRepo.findByOwnerId(loggedInUser.getId());
            Dog selectedDog = appointmentRepo.findById(changeAppointmentId).get().getDog();
            model.addAttribute("dogs", dogs);
            model.addAttribute("selectedDog", selectedDog);
            model.addAttribute("changeAppointmentId", changeAppointmentId);
        }

        return "calendar";
    }


}
