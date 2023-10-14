package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Admin;
import com.bookitapp.Book.It.models.EmailDetails;
import com.bookitapp.Book.It.models.Review;
import com.bookitapp.Book.It.repositories.AdminRepository;
import com.bookitapp.Book.It.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final ReviewRepository reviewRepo;

    @GetMapping
    String homePage(Model model) {
        List<Review> lorenasReviews = reviewRepo.findByGroomerId(1L);
        List<Review> karasReviews = reviewRepo.findByGroomerId(2L);
        model.addAttribute("lorenasReviews", lorenasReviews);
        model.addAttribute("karasReviews", karasReviews);
        return "index";
    }

}
