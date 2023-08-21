package com.bookitapp.Book.It.controllers;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @GetMapping
    String calendarPage() {
        return "calendar";
    }

    @GetMapping("/{changeAppointmentId}")
    String calendarPage(
            @PathVariable("changeAppointmentId") @Nullable Long changeAppointmentId,
            Model model) {

        model.addAttribute("changeAppointmentId", changeAppointmentId);

        return "calendar";
    }


}
