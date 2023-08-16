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

@Controller
@RequestMapping("/groomers")
@RequiredArgsConstructor
public class GroomerController {

    private final AppointmentRepository appointmentRepo;

    @GetMapping
    ResponseBody getGroomerDetails() {
        return null;
    }

    @GetMapping(value = "/getGroomerAvailability/{groomerId}", produces = "application/json")
    @ResponseBody
    public List<List<List<String>>> getGroomerAvailability(@PathVariable("groomerId") Long groomerId) {
        List<List<List<String>>> availabilityByWeek = new ArrayList<>();

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime dateOfEndOfWeek = currentDate.plusDays(21); // For three weeks

        List<Appointment> appointmentsForGroomerBetweenForTheWeek = appointmentRepo.findAppointmentsForGroomerForTheWeek(groomerId, currentDate, dateOfEndOfWeek);

        LocalDateTime currentWeekStart = currentDate;
        while (currentWeekStart.isBefore(dateOfEndOfWeek)) {
            List<List<String>> weekAvailability = new ArrayList<>();

            LocalDateTime currentTimeSlot = currentWeekStart.with(LocalTime.of(9, 0)); // Starting from 9:00 AM
            while (currentTimeSlot.isBefore(currentWeekStart.plusDays(7))) {
                String dateString = currentTimeSlot.toLocalTime().toString();
                weekAvailability.add(Collections.singletonList(dateString));
                currentTimeSlot = currentTimeSlot.plusHours(1); // Increment by 1 hour
            }

            // Remove already booked time slots for the current week
            for (Appointment appointment : appointmentsForGroomerBetweenForTheWeek) {
                LocalDateTime appointmentDateTime = appointment.getAppointmentTime();
                if (appointmentDateTime.isAfter(currentWeekStart) && appointmentDateTime.isBefore(currentWeekStart.plusDays(7).plusNanos(1))) {
                    String dateString = appointmentDateTime.toLocalTime().toString();
                    weekAvailability.get(appointmentDateTime.getDayOfWeek().getValue() - 1).remove(dateString);
                }
            }

            availabilityByWeek.add(weekAvailability);
            currentWeekStart = currentWeekStart.plusDays(7);
        }

        return availabilityByWeek;
    }


}
