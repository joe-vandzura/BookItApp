package com.bookitapp.Book.It.repositories;

import com.bookitapp.Book.It.models.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {

    List<Dog> findByOwnerId(Long ownerId);

}
