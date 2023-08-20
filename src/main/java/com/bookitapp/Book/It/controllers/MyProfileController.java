package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.UserRepository;
import com.bookitapp.Book.It.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/my-profile")
@RequiredArgsConstructor
public class MyProfileController {

    private final AuthService authService;
    private final AppointmentRepository appointmentRepo;

    @GetMapping("/appointments")
    public String myAppointmentsPage(Model model) {
        boolean userIsAuthenticated = authService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Appointment> appointments = appointmentRepo.findByUserId(loggedInUser.getId());
            model.addAttribute("appointments", appointments);
            return "profile/appointments";
        } else {
            return "redirect:/login";
        }

    }

}
