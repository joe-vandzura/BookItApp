package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/groomers")
@RequiredArgsConstructor
public class GroomerController {

    private final AppointmentRepository appointmentRepo;

    @GetMapping
    ResponseBody getGroomerDetails() {
        return null;
    }

    @GetMapping(value = "/{groomerId}/availability", produces = "application/json")
    @ResponseBody
    public List<List<String>> getGroomerAvailability(@PathVariable("groomerId") Long groomerId) {
        List<List<String>> availabilityForWeek = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime dateOfEndOfWeek = currentDateTime.plusDays(7); // For one week

        // get all appointment times for the current week being displayed
        List<String> appointmentTimes = appointmentRepo.findAppointmentsForGroomerForTheWeek(groomerId, currentDateTime, dateOfEndOfWeek).stream()
                .map(Appointment::getAppointmentTime)
                .map(dateTime -> dateTime.plusHours(5))
                .map(LocalDateTime::toString)
                .toList();

        LocalDateTime currentTimeSlot = currentDateTime.with(LocalTime.of(9, 0)); // Starting from 9:00 AM

        // generate the appointment times for the week being displayed
        for (int i = 0; i < 7; i++) {
            List<String> dayOfTimes = new ArrayList<>();
            for (int j = 0; j < 10; j++) {

                String dateString = currentTimeSlot.toString();
                dayOfTimes.add(dateString);
                currentTimeSlot = currentTimeSlot.plusHours(1);

            }
            availabilityForWeek.add(dayOfTimes);
            // if currentTimeSlot is 6:00 PM, change it to 9:00 AM in the next day
            if (currentTimeSlot.getHour() > 18) {
                currentTimeSlot = currentTimeSlot.plusHours(14);
            }
        }

        // Print the appointment times and list of available times before removal
        System.out.println("Appointment Times: " + appointmentTimes);
        System.out.println("Available Times (Before Removal): " + availabilityForWeek);

        // Remove the appointment times that are already taken from the list of available times
        availabilityForWeek.forEach(listOfDateTimeStrings ->
                listOfDateTimeStrings.removeIf(dateTimeString -> appointmentTimes.contains(dateTimeString)));


        // Print the list of available times after removal
        System.out.println("Available Times (After Removal): " + availabilityForWeek);

        return availabilityForWeek;
    }



}
