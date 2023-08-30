package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Dog;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.DogRepository;
import com.bookitapp.Book.It.services.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/my-profile")
@RequiredArgsConstructor
public class MyProfileController {

    private final AppointmentRepository appointmentRepo;
    private final DogRepository dogRepo;

    @GetMapping("/appointments")
    public String myAppointmentsPage(Model model) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Appointment> appointments = appointmentRepo.findByUserIdOrderByAppointmentTimeAsc(loggedInUser.getId());

            model.addAttribute("appointments", appointments);
            return "profile/appointments";
        } else {
            return "redirect:/login";
        }

    }

    @GetMapping("/account")
    public String accountPage(Model model) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("dog", new Dog());
            List<Dog> dogs = dogRepo.findByOwnerId(loggedInUser.getId());

            // dog breeds api call to get breed options
            try {
                URL url = new URL("https://dog.ceo/api/breeds/list/all");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    List<String> breeds = new ArrayList<>();
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(String.valueOf(response));
                    JsonNode messageNode = jsonNode.get("message");
                    messageNode.fieldNames().forEachRemaining(breeds::add);

                    model.addAttribute("dogBreeds", breeds);
                    System.out.println(breeds);
                } else {
                    System.out.println("API request failed with response code: " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            model.addAttribute("dogs", dogs);
        } else {
            return "redirect:/login";
        }
        return "profile/account";
    }

}
