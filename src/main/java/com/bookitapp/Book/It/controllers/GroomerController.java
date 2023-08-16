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

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime dateOfEndOfWeek = currentDate.plusDays(7); // For one week

        List<LocalDateTime> appointmentTimes = appointmentRepo.findAppointmentsForGroomerForTheWeek(groomerId, currentDate, dateOfEndOfWeek).stream()
                .map(Appointment::getAppointmentTime) // Map Appointment objects to their appointmentTime property
                .collect(Collectors.toList()); // Collect the LocalDateTime objects into a list

        LocalDateTime currentTimeSlot = currentDate.with(LocalTime.of(9, 0)); // Starting from 9:00 AM
        while (currentTimeSlot.isBefore(dateOfEndOfWeek)) {
            List<String> dayOfTimes = new ArrayList<>();
            for (int i = 0; i < 10; i++) {

                if (!appointmentTimes.contains(currentTimeSlot)) {
                    String dateString = currentTimeSlot.toLocalTime().toString();
                    dayOfTimes.add(dateString);
                    currentTimeSlot = currentTimeSlot.plusHours(1);
                }

            }
            availabilityForWeek.add(dayOfTimes);
            // if currentTimeSlot is 6:00 PM, change it to 9:00 AM in the next day
            if (currentTimeSlot.getHour() > 18) {
                currentTimeSlot = currentTimeSlot.plusHours(14);
            }
        }

        System.out.println(appointmentTimes);
        System.out.println(currentTimeSlot);

        return availabilityForWeek;
    }



}
