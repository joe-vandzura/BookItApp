package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Review;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.ReviewRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final AppointmentRepository appointmentRepo;
    private final ReviewRepository reviewRepo;

    @GetMapping
    public String showReviewPage(
            Model model,
            @RequestParam(name = "appointment-id") Long appointmentId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review review = reviewRepo.findByAppointmentIdAndReviewerId(appointmentId, loggedInUser.getId());

        if (review != null) {
            return "redirect:/reviews/" + review.getId();
        } else {
            Appointment appointment = appointmentRepo.findById(appointmentId).get();
            model.addAttribute("appointment", appointment);
            return "review";
        }
    }

    @PostMapping
    public String createReview(
            @RequestParam(name = "appointment-id") Long appointmentId,
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

    @GetMapping("/{reviewId}")
    public String showReviewPage(@PathVariable(name = "reviewId") Long reviewId) {
        return "review";
    }

}
