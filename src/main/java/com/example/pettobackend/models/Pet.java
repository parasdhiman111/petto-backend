package com.example.pettobackend.models;


import com.example.pettobackend.models.enums.Breed;
import com.example.pettobackend.models.enums.Gender;
import com.example.pettobackend.models.enums.PetCategory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "pets")
public class Pet {

    @Id
    private String petId;
    private String userId;
    private String petUserName;
    private String name;
    private PetCategory petCategory;
    private Breed breed;
    private Gender gender;
    private String profileUrl;
    private String bio;



}
