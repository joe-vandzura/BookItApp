package com.bookitapp.Book.It.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDetails {

    private String recipient;
    private String msgBody;
    private String subject;
    private Long userId;
    private LocalDateTime appointmentTime;
    private String groomerName;
    private String dogName;
    private int viewCount;
}