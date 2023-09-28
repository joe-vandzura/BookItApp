package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.EmailDetails;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.UserRepository;
import com.bookitapp.Book.It.services.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.context.Context;

@Controller
public class EmailController {

    @Autowired
    private EmailServiceImpl emailService;

    private final UserRepository userRepo;

    EmailController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @PostMapping("/sendMail")
    public void sendEmail(@RequestBody EmailDetails details) {
        Context context = new Context();
        context.setVariable("message", details.getMsgBody());

        emailService.sendEmailWithHtmlTemplate(details, "email/email-template", context);
    }

    @GetMapping("/email-verified/{userId}")
    public String emailVerifiedPage(@PathVariable(name = "userId") Long userId) {
        User actualUser = userRepo.findById(userId).get();
        actualUser.setEmailVerified(true);
        userRepo.save(actualUser);
        return "email-verification";
    }

}
