package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.auth.JwtTokenProvider;
import com.altech.electronicstore.assignment.auth.JwtAuthenticationResponse;
import com.altech.electronicstore.assignment.dto.LoginRequest;
import com.altech.electronicstore.assignment.dto.RegistrationRequest;
import com.altech.electronicstore.assignment.dto.UserProfile;
import com.altech.electronicstore.assignment.auth.UserRole;
import com.altech.electronicstore.assignment.repositories.UserRepository;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
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

    @Operation(
        summary = "Authenticate user and return JWT token",
        description = "Authenticates a user using username and password. Returns a JWT token if authentication is successful."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated. JWT token returned."),
        @ApiResponse(responseCode = "401", description = "Invalid credentials.")
    })
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

    @Operation(
        summary = "Register a new user",
        description = "Registers a new user with the provided username and password. Returns the created user profile."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully."),
        @ApiResponse(responseCode = "400", description = "Username already exists.")
    })
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

    @Operation(
        summary = "Register a new admin user",
        description = "Registers a new admin user. Only accessible by users with ADMIN role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin user registered successfully."),
        @ApiResponse(responseCode = "400", description = "Username already exists."),
        @ApiResponse(responseCode = "403", description = "Access denied. Only admins can register new admins.")
    })
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
