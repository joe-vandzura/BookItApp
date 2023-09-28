package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Admin;
import com.bookitapp.Book.It.models.EmailDetails;
import com.bookitapp.Book.It.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final EmailController emailController;
    private final AdminRepository adminRepo;

    @GetMapping
    String homePage() {
        EmailDetails details = new EmailDetails();
        Admin adminColumn = adminRepo.findById(1L).get();
        details.setRecipient(adminColumn.getAdminEmail());
        int viewCount = adminColumn.getViewCount() + 1;
        adminColumn.setViewCount(viewCount);
        adminRepo.save(adminColumn);
        details.setViewCount(viewCount);
        details.setSubject("Project Was Viewed!");
        emailController.sendBookItAppViewNotificiation(details);
        return "index";
    }

}
