package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Dog;
import com.bookitapp.Book.It.models.DogDTO;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.DogRepository;
import com.bookitapp.Book.It.repositories.UserRepository;
import com.bookitapp.Book.It.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dogs")
@RequiredArgsConstructor
public class DogController {

    private final DogRepository dogRepo;
    private final UserRepository userRepo;

    @PostMapping
    public String addDog(
            @ModelAttribute Dog dog) {
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

    @GetMapping(value = "/{dogId}", produces = "application/json")
    public ResponseEntity<DogDTO> getDogData(@PathVariable("dogId") Long dogId) {
        Dog dog = dogRepo.findById(dogId).orElse(null);

        if (dog != null) {
            DogDTO dogDTO = new DogDTO(dog);
            return ResponseEntity.ok(dogDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{dogId}")
    public ResponseEntity<Void> updateDog(
            @PathVariable("dogId") Long dogId,
            @RequestBody DogDTO updatedDogData
    ) {
        Dog dog = dogRepo.findById(dogId).get();
        dog.setName(updatedDogData.getName());
        dog.setBreed(updatedDogData.getBreed());
        dog.setAge(updatedDogData.getAge());
        dog.setSex(updatedDogData.getSex());
        dog.setRabiesVaccinationStatus(updatedDogData.getHasRabiesVaccination());

        dogRepo.save(dog);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{dogId}")
    public String changeDog(
            @PathVariable("dogId") Long dogId,
            @RequestParam("_method") @Nullable String method,
            @RequestParam("name") @Nullable String name,
            @RequestParam("breed") @Nullable String breed,
            @RequestParam("age") @Nullable Integer age,
            @RequestParam("sex") @Nullable Character sex,
            @RequestParam("rabiesVaccinationStatus") @Nullable Boolean rabiesVaccinationStatus
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
            if (rabiesVaccinationStatus == null) {
                rabiesVaccinationStatus = false;
            }
            dogToChange.setRabiesVaccinationStatus(rabiesVaccinationStatus);
            dogRepo.save(dogToChange);
        }

        return "redirect:/my-profile/account";
    }


}
