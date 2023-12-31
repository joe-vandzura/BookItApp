package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.Dog;
import com.bookitapp.Book.It.models.Review;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.repositories.DogRepository;
import com.bookitapp.Book.It.repositories.ReviewRepository;
import com.bookitapp.Book.It.repositories.UserRepository;
import com.bookitapp.Book.It.services.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/my-profile")
@RequiredArgsConstructor
public class MyProfileController {

    private final AppointmentRepository appointmentRepo;
    private final UserRepository userRepo;
    private final DogRepository dogRepo;
    private final ReviewRepository reviewRepo;

    @GetMapping("/appointments")
    public String myAppointmentsPage(Model model) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Appointment> upcomingAppointments = appointmentRepo.getFutureAppointmentsByUserId(loggedInUser.getId(), ZonedDateTime.now(ZoneId.of("UTC")));
            List<Appointment> prevAppointments = appointmentRepo.getPastAppointmentsByUserId(loggedInUser.getId(), ZonedDateTime.now(ZoneId.of("UTC")));

            model.addAttribute("upcomingAppointments", upcomingAppointments);
            model.addAttribute("prevAppointments", prevAppointments);
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
            User actualLoggedInUser = userRepo.findById(loggedInUser.getId()).get();
            model.addAttribute("user", actualLoggedInUser);
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

    @PostMapping("/account")
    public String editProfile(
            @RequestParam("first-name") String firstName,
            @RequestParam("last-name") String lastName,
            @RequestParam("username") String username,
            @RequestParam("email") String email
    ) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User actualUser = userRepo.findById(loggedInUser.getId()).get();
        if (!actualUser.getEmail().equals(email)) {
            actualUser.setEmailVerified(false);
            actualUser.setEmail(email);
        }
        actualUser.setFirstName(firstName);
        actualUser.setLastName(lastName);
        actualUser.setUsername(username);
        userRepo.save(actualUser);

        return "redirect:/my-profile/account?saved";
    }

    @GetMapping("/account/change-password")
    public String changePassword(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User actualUser = userRepo.findById(loggedInUser.getId()).get();
        model.addAttribute("userId", actualUser.getId());
        model.addAttribute("isLoggedIn", true);
        return "reset-password";
    }

    @GetMapping("/reviews")
    public String showMyReviewsPage(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Review> listOfUsersReviews = reviewRepo.findByReviewerId(loggedInUser.getId());
        model.addAttribute("reviews", listOfUsersReviews);
        return "profile/reviews";
    }

}
