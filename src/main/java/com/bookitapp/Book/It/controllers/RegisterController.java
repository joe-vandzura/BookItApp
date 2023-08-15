package com.bookitapp.Book.It.controllers;

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

    @GetMapping
    String registerPage(Model model) {
        model.addAttribute("new_user", new User());
        return "register";
    }

    @PostMapping
    String register(User newUser) {
        String hash = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hash);
        userRepo.save(newUser);
        return "login";
    }

}
