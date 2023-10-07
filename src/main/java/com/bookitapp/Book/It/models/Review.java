package com.bookitapp.Book.It.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "appointment_id")
    private Appointment appointment;

    @Column(name = "rating")
    private int rating;

    @Column(name = "description")
    private String description;

}
