package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.EmailDetails;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.UserRepository;
import com.bookitapp.Book.It.services.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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


    @PostMapping("/sendVerificationEmail")
    public void sendVerificationEmail(@RequestBody EmailDetails details) {
        Context context = new Context();
        context.setVariable("userId", details.getUserId());

        emailService.sendEmailWithHtmlTemplate(details, "email/verification-email", context);
    }

    @PostMapping("/resendVerificationEmail")
    public void resendVerificationEmail() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User actualLoggedInUser = userRepo.findById(loggedInUser.getId()).get();

        Context context = new Context();
        context.setVariable("userId", actualLoggedInUser.getId());

        EmailDetails details = new EmailDetails();
        details.setRecipient(loggedInUser.getEmail());
        details.setSubject("Verify Your Email");
        emailService.sendEmailWithHtmlTemplate(details, "email/verification-email", context);
    }

    @PostMapping("/sendConfirmationEmail")
    public void sendConfirmationEmail(@RequestBody EmailDetails details) {
        Context context = new Context();
        context.setVariable("appointmentTime", details.getAppointmentTime());
        context.setVariable("groomerName", details.getGroomerName());
        context.setVariable("dogName", details.getDogName());
        emailService.sendEmailWithHtmlTemplate(details, "email/confirmation-email", context);
    }

    @PostMapping("/sendChangeAppointmentConfirmationEmail")
    public void sendChangeAppointmentConfirmationEmail(@RequestBody EmailDetails details) {
        Context context = new Context();
        context.setVariable("appointmentTime", details.getAppointmentTime());
        context.setVariable("groomerName", details.getGroomerName());
        context.setVariable("dogName", details.getDogName());
        emailService.sendEmailWithHtmlTemplate(details, "email/appointment-change-email", context);
    }

    @GetMapping("/email-verified/{userId}")
    public String emailVerifiedPage(@PathVariable(name = "userId") Long userId) {
        User actualUser = userRepo.findById(userId).get();
        actualUser.setEmailVerified(true);
        userRepo.save(actualUser);
        return "email-verification";
    }

    @PostMapping("/bookItAppViewNotificiation")
    public void sendBookItAppViewNotificiation(@RequestBody EmailDetails details) {
        Context context = new Context();
        context.setVariable("viewCount", details.getViewCount());
        emailService.sendEmailWithHtmlTemplate(details, "email/view-notification-email", context);
    }

    @PostMapping("/resetPassword")
    public void sendResetPasswordEmail(@RequestBody EmailDetails details) {
        Context context = new Context();
        context.setVariable("userId", details.getUserId());
        context.setVariable("securityToken", details.getSecurityToken());
        emailService.sendEmailWithHtmlTemplate(details, "email/reset-password-email", context);
    }



}
