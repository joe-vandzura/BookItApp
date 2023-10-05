package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final AppointmentRepository appointmentRepo;

    @GetMapping("/{appointmentId}")
    public String showReviewPage(
            Model model,
            @PathVariable(name = "appointmentId") Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        model.addAttribute("appointment", appointment);
        return "review";
    }

}
