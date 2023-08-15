package com.bookitapp.Book.It.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/groomers")
public class GroomerController {

    @GetMapping
    ResponseBody getGroomerDetails() {
        return null;
    }

}
