package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Review;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.ReviewRepository;
import com.bookitapp.Book.It.repositories.UserRepository;
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
    private final UserRepository userRepo;

    @GetMapping
    public String showReviewPage(
            Model model,
            @RequestParam(name = "appointment-id") Long appointmentId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review review = reviewRepo.findByAppointmentIdAndReviewerId(appointmentId, loggedInUser.getId());
        if (review != null) {
            model.addAttribute("existingReview", review);
        }
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        model.addAttribute("appointment", appointment);
        return "review";
    }

    @PostMapping
    public String createReview(
            @RequestParam(name = "appointment-id") Long appointmentId,
            @RequestParam(name = "rating") Integer rating,
            @RequestParam(name = "description") String description) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User actualLoggedInUser = userRepo.findById(loggedInUser.getId()).get();
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        Review newReview = new Review();
        newReview.setAppointment(appointment);
        newReview.setGroomer(appointment.getGroomer());
        newReview.setRating(rating);
        newReview.setDescription(description);
        newReview.setReviewer(actualLoggedInUser);
        reviewRepo.save(newReview);
        return "redirect:/my-profile/reviews";
    }

    @PostMapping("/delete")
    public String deleteReview(@RequestParam(name = "review-id") Long reviewId) {
        Review review = reviewRepo.findById(reviewId).get();
        reviewRepo.delete(review);
        return "redirect:/my-profile/reviews?deleted";
    }

}
