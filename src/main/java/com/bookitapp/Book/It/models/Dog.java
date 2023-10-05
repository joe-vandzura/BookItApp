package com.bookitapp.Book.It.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dogs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "breed", nullable = false)
    private String breed;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "sex", nullable = false)
    private char sex;

    @Column(name = "rabies_vaccination", nullable = false)
    private Boolean rabiesVaccinationStatus;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User owner;

    public String toString() {
        return this.name;
    }

}
