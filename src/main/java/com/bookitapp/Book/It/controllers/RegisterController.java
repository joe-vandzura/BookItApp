package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.EmailDetails;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final EmailController emailController;

    @GetMapping
    String registerPage(Model model) {
        model.addAttribute("new_user", new User());
        return "register";
    }

    @PostMapping
    String register(User newUser) {
        String hash = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hash);
        newUser.setEmailVerified(false);
        userRepo.save(newUser);
        User actualNewUser = userRepo.findByUsername(newUser.getUsername());
        EmailDetails details = new EmailDetails();
        details.setRecipient(newUser.getEmail());
        details.setUserId((actualNewUser.getId()));
        details.setSubject("Verify Your Email");
        emailController.sendVerificationEmail(details);
        return "login";
    }

}
