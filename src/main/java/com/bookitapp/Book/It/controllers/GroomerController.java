package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import com.bookitapp.Book.It.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;

@Controller
@RequestMapping("/groomers")
@RequiredArgsConstructor
public class GroomerController {

    private final AppointmentRepository appointmentRepo;
    private final AuthService authService;

    @GetMapping
    ResponseBody getGroomerDetails() {
        return null;
    }

    @GetMapping(value = "/{groomerId}/availability", produces = "application/json")
    @ResponseBody
    public List<List<String>> getGroomerAvailability(
            @PathVariable("groomerId") Long groomerId,
            @RequestParam("amountOfWeekOffset") int amountOfWeekOffset) {
        boolean userIsAuthenticated = AuthService.isLoggedIn();

        if (userIsAuthenticated) {


            // check if offset is within 3 month range
            if (amountOfWeekOffset < 0) {
                amountOfWeekOffset = 0;
            } else if (amountOfWeekOffset > 98) {
                amountOfWeekOffset = 98;
            }

            List<List<String>> availabilityForWeek = new ArrayList<>();

            ZonedDateTime utcCurrentDateTime = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(amountOfWeekOffset);
            ZonedDateTime dateOfEndOfWeek = utcCurrentDateTime.plusDays(7); // For one week

            // get all appointment times for the current week being displayed
            List<String> appointmentTimes = appointmentRepo.findAppointmentsForGroomerForTheWeek(groomerId, utcCurrentDateTime, dateOfEndOfWeek).stream()
                    .map(Appointment::getAppointmentTime)
                    .map(LocalDateTime::toString) // Convert to LocalDateTime for display
                    .toList();

            LocalDateTime currentTimeSlot = utcCurrentDateTime.toLocalDateTime().with(LocalTime.of(9, 0)); // Starting from 9:00 AM

            // generate the appointment times for the week being displayed
            for (int i = 0; i < 7; i++) {
                List<String> dayOfTimes = new ArrayList<>();
                for (int j = 0; j < 10; j++) {

                    if (currentTimeSlot.isAfter(LocalDateTime.now().plusHours(2))) {
                        String dateString = currentTimeSlot.toString();
                        dayOfTimes.add(dateString);
                    }
                    currentTimeSlot = currentTimeSlot.plusHours(1);

                }
                availabilityForWeek.add(dayOfTimes);
                // if currentTimeSlot is 6:00 PM, change it to 9:00 AM in the next day
                if (currentTimeSlot.getHour() > 18) {
                    currentTimeSlot = currentTimeSlot.plusHours(14);
                }
            }

            // Remove the appointment times that are already taken from the list of available times
            availabilityForWeek.forEach(listOfDateTimeStrings ->
                    listOfDateTimeStrings.removeIf(appointmentTimes::contains));

            availabilityForWeek.forEach(listOfDateTimeStrings ->
                    listOfDateTimeStrings.removeIf(appointmentTimes::contains));
            return availabilityForWeek;
        } else {
            System.out.println("User is not logged in");
            return null;
        }
    }



}
