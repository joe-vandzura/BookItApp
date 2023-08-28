package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Dog;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.DogRepository;
import com.bookitapp.Book.It.repositories.UserRepository;
import com.bookitapp.Book.It.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dogs")
@RequiredArgsConstructor
public class DogController {

    private final DogRepository dogRepo;
    private final UserRepository userRepo;

    @PostMapping
    public String addDog(@ModelAttribute Dog dog) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User owner = userRepo.findById(loggedInUser.getId()).get();
            dog.setOwner(owner);
            dogRepo.save(dog);
            return "redirect:/my-profile/account";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/{dogId}")
    public String changeDog(
            @PathVariable("dogId") Long dogId,
            @RequestParam("_method") String method,
            @RequestParam("name") String name,
            @RequestParam("breed") String breed,
            @RequestParam("age") int age,
            @RequestParam("sex") char sex,
            @RequestParam("rabiesVaccinationStatus") boolean rabiesVaccinationStatus
            ) {

        if ("DELETE".equals(method)) {
            dogRepo.deleteById(dogId);
            return "redirect:/my-profile/account";
        } else {
            Dog dogToChange = dogRepo.findById(dogId).get();
            dogToChange.setName(name);
            dogToChange.setBreed(breed);
            dogToChange.setAge(age);
            dogToChange.setSex(sex);
            dogToChange.setRabiesVaccinationStatus(rabiesVaccinationStatus);
            dogRepo.save(dogToChange);
        }

        return "redirect:/my-profile/account";
    }

}
