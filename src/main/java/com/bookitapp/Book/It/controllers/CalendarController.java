package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Dog;
import com.bookitapp.Book.It.models.User;
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

import java.util.List;

@Controller
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final DogRepository dogRepo;

    @GetMapping
    String calendarPage(Model model) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Dog> dogs = dogRepo.findByOwnerId(loggedInUser.getId());
            model.addAttribute("dogs", dogs);
            return "calendar";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/{changeAppointmentId}")
    String calendarPage(
            @PathVariable("changeAppointmentId") @Nullable Long changeAppointmentId,
            Model model) {

        model.addAttribute("changeAppointmentId", changeAppointmentId);

        return "calendar";
    }


}
