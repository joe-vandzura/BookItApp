package com.bookitapp.Book.It.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Appointment> appointments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Dog> dogs;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reviewer")
    private List<Review> reviews;

    public String toString() {
        return this.firstName;
    }

    public User(User copy) {
        id = copy.id;
        username = copy.username;
        email = copy.email;
        password = copy.password;
        firstName = copy.firstName;
        lastName = copy.lastName;
        appointments = copy.appointments;
        dogs = copy.dogs;
        emailVerified = copy.emailVerified;
        tokens = copy.tokens;
        reviews = copy.reviews;
    }
}
