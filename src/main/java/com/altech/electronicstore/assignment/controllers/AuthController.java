package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.auth.JwtTokenProvider;
import com.altech.electronicstore.assignment.auth.JwtAuthenticationResponse;
import com.altech.electronicstore.assignment.models.LoginRequest;
import com.altech.electronicstore.assignment.models.RegistrationRequest;
import com.altech.electronicstore.assignment.models.UserProfile;
import com.altech.electronicstore.assignment.auth.UserRole;
import com.altech.electronicstore.assignment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController{

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<UserProfile> register(@RequestBody RegistrationRequest registrationRequest) {
        if (userRepository.existsUserByUsername(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        UserProfile user = new UserProfile();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(UserRole.USER); // Default role

        UserProfile savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfile> registerAdmin(@RequestBody RegistrationRequest registrationRequest) {
        if (userRepository.existsUserByUsername(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        UserProfile user = new UserProfile();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(UserRole.ADMIN);

        UserProfile savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}
