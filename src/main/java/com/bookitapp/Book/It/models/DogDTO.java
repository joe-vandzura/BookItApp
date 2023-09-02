package com.bookitapp.Book.It.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class DogDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("breed")
    private String breed;

    @JsonProperty("age")
    private int age;

    @JsonProperty("sex")
    private char sex;

    @JsonProperty("hasRabiesVaccination")
    private Boolean hasRabiesVaccination;

    public DogDTO(){};

    public DogDTO(Dog dog) {
        this.id = dog.getId();
        this.name = dog.getName();
        this.breed = dog.getBreed();
        this.age = dog.getAge();
        this.sex = dog.getSex();
        this.hasRabiesVaccination = dog.getRabiesVaccinationStatus();
    }

}