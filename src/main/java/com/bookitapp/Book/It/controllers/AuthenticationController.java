package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepository userRepo;

    @GetMapping("/login")
    String loginPage() {
        return "login";
    }

    @GetMapping(value = "/username-check/{usernameInput}", produces = "application/json")
    @ResponseBody
    public boolean checkIfUsernameExists(@PathVariable(name = "usernameInput") String usernameInput){
        boolean usernameExists = userRepo.findByUsername(usernameInput) != null;
        return usernameExists;
    }

    @GetMapping(value = "/email-check/{emailInput}", produces = "application/json")
    @ResponseBody
    public boolean checkIfEmailAlreadyUsed(@PathVariable(name = "emailInput") String emailInput){
        boolean emailBeingUsed = userRepo.findUserByEmail(emailInput) != null;
        return emailBeingUsed;
    }

}
