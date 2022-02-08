package com.example.pettobackend.controllers;

import com.example.pettobackend.dto.requests.LoginRequest;
import com.example.pettobackend.dto.requests.SignupRequest;
import com.example.pettobackend.dto.responses.JwtResponse;
import com.example.pettobackend.dto.responses.MessageResponse;
import com.example.pettobackend.dto.responses.UserDetailsResponse;
import com.example.pettobackend.models.Role;
import com.example.pettobackend.models.User;
import com.example.pettobackend.models.enums.Gender;
import com.example.pettobackend.models.enums.Roles;
import com.example.pettobackend.repositories.PasswordConfirmationTokenRepository;
import com.example.pettobackend.repositories.RoleRepository;
import com.example.pettobackend.repositories.UserRepository;
import com.example.pettobackend.security.jwt.JwtUtils;
import com.example.pettobackend.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${paras.app.jwtSecret}")
    public String jwtSecret;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${paras.app.serviceUrl}")
    private String serviceUrl;

    @PostMapping("/login")
    public ResponseEntity<?> loginRequest(@RequestBody LoginRequest loginRequest)
    {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtUtils.generateToken(authentication);
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getUserId(),
                userDetails.getUsername(),
                roles

        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpRequest(@RequestBody SignupRequest signUpRequest)
    {
        if(userRepository.existsByEmail(signUpRequest.getEmail()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email already exists"));
        }
        User newUser=new User();
        newUser.setFirstName(signUpRequest.getFirstName());
        newUser.setLastName(signUpRequest.getLastName());
        newUser.setEmail(signUpRequest.getEmail());
        if(signUpRequest.getGender().equalsIgnoreCase("male"))
            newUser.setGender(Gender.MALE);
        else if(signUpRequest.getGender().equalsIgnoreCase("female"))
            newUser.setGender(Gender.FEMALE);
        else
            newUser.setGender(Gender.MALE);
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(Roles.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(adminRole);
        newUser.setRoles(roles);
        userRepository.save(newUser);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUserDetails(@RequestHeader String authorization)
    {
        Optional<User> user=userRepository.findById(getUserIdFromToken(authorization.substring(7)));

        if(user.isPresent())
        {
            UserDetailsResponse response=new UserDetailsResponse(
                    user.get().getFirstName(),
                    user.get().getLastName(),
                    user.get().getEmail(),
                    user.get().getGender().toString()
            );

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }


    private String getUserIdFromToken(String token)
    {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getId();
    }



}
