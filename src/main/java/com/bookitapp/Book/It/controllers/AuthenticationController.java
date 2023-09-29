package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.EmailDetails;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepository userRepo;
    private final EmailController emailController;

    @GetMapping("/login")
    String loginPage() {
        return "login";
    }

    @GetMapping(value = "/username-check/{usernameInput}", produces = "application/json")
    @ResponseBody
    public boolean checkIfUsernameExists(@PathVariable(name = "usernameInput") String usernameInput) {
        boolean usernameExists = userRepo.findByUsername(usernameInput) != null;
        return usernameExists;
    }

    @GetMapping(value = "/email-check/{emailInput}", produces = "application/json")
    @ResponseBody
    public boolean checkIfEmailAlreadyUsed(@PathVariable(name = "emailInput") String emailInput) {
        boolean emailBeingUsed = userRepo.findUserByEmail(emailInput) != null;
        return emailBeingUsed;
    }

    @GetMapping("/forgot-password")
    String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    void submitForgotPasswordForm(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");

        User user = userRepo.findByUsername(username);

        if (user != null && !user.getEmail().equals(email)) {
            EmailDetails details = new EmailDetails();
            details.setRecipient(user.getEmail());
            details.setUserId((user.getId()));
            details.setSubject("Reset Your Password");
            emailController.sendResetPasswordEmail(details);
        }
    }

    @GetMapping("/reset-password/{userId}")
    String showResetPasswordPage(
            Model model,
            @PathVariable(name = "userId") Long userId) {
        model.addAttribute("userId", userId);
        return "reset-password";
    }

}
