package com.example.pettobackend.controllers;


import com.example.pettobackend.dto.requests.AddPetRequest;
import com.example.pettobackend.dto.responses.MessageResponse;
import com.example.pettobackend.models.Pet;
import com.example.pettobackend.models.enums.Breed;
import com.example.pettobackend.models.enums.Gender;
import com.example.pettobackend.models.enums.PetCategory;
import com.example.pettobackend.repositories.PetRepository;
import com.example.pettobackend.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @Value("${paras.app.jwtSecret}")
    public String jwtSecret;

    @PostMapping("/pet")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addNewPet(@RequestHeader String authorization,@RequestBody AddPetRequest addPetRequest)
    {
//        if(petRepository.findByPetUserName(addPetRequest.getPetUserName()).isPresent())
//        {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username already Present"));
//        }
        Pet pet=new Pet();
        pet.setUserId(getUserIdFromToken(authorization.substring(7)));
        pet.setName(addPetRequest.getPetName());
        //pet.setPetUserName(addPetRequest.getPetUserName());
        pet.setBreed(Breed.valueOf(addPetRequest.getPetBreed()));
        pet.setPetCategory(PetCategory.valueOf(addPetRequest.getPetCategory()));
        pet.setGender(Gender.valueOf(addPetRequest.getPetGender()));
        pet.setProfileUrl(addPetRequest.getPetProfileUrl());
        pet.setBio(addPetRequest.getPetBio());
        pet.setProfileUrl("https://i.pinimg.com/originals/c3/6e/a4/c36ea4eb6b5af4332c7f1f11eff88015.png");
        petRepository.save(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(pet);
    }

    @GetMapping("/pet/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllPetsByUser(@PathVariable String userId)
    {
        List<Pet> pets= petRepository.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(pets);
    }




    private String getUserIdFromToken(String token)
    {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getId();
    }

}
