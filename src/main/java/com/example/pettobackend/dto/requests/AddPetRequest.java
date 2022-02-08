package com.example.pettobackend.dto.requests;

import lombok.Data;

@Data
public class AddPetRequest {

    private String petName;
    //private String petUserName;
    private String petBreed;
    private String petCategory;
    private String petGender;
    private String petBio;
    private String petProfileUrl;


}
