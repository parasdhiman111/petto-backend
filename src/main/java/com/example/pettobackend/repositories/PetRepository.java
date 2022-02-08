package com.example.pettobackend.repositories;

import com.example.pettobackend.models.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends MongoRepository<Pet,String> {

    List<Pet> findAllByUserId(String userId);


}
