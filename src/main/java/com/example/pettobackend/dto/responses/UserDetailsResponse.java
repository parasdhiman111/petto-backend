package com.example.pettobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String gender;

}
