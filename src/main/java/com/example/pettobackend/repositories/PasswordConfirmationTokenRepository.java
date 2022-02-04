package com.example.pettobackend.repositories;

import com.example.pettobackend.models.PasswordConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordConfirmationTokenRepository extends MongoRepository<PasswordConfirmationToken,String> {

    PasswordConfirmationToken findByConfirmationToken(String confirmationToken);

}
