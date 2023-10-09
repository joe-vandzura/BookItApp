package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Review;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final AppointmentRepository appointmentRepo;
    private final ReviewRepository reviewRepo;

    @GetMapping("/{appointmentId}")
    public String showReviewPage(
            Model model,
            @PathVariable(name = "appointmentId") Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        model.addAttribute("appointment", appointment);
        return "review";
    }

    @PostMapping("/{appointmentId}")
    public String createReview(
            @PathVariable(name = "appointmentId") Long appointmentId,
            @RequestParam(name = "rating") Integer rating,
            @RequestParam(name = "description") String description) {
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        Review newReview = new Review();
        newReview.setAppointment(appointment);
        newReview.setRating(rating);
        newReview.setDescription(description);
        reviewRepo.save(newReview);
        return "redirect:/my-profile/account";
    }

}
