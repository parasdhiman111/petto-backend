package com.example.pettobackend.dto.requests;

import lombok.Data;

@Data
public class SignupRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String password;
    public SignupRequest(String firstName, String lastName, String email,String gender, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender=gender;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
