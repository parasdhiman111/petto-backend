package com.example.pettobackend.repositories;

import com.example.pettobackend.models.Role;
import com.example.pettobackend.models.enums.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role,String> {
    Optional<Role> findByName(Roles role);
}
