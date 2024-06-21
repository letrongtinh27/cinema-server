package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.auth.AuthenticationRequest;
import com.edu.hcmuaf.springserver.auth.AuthenticationResponse;
import com.edu.hcmuaf.springserver.auth.RegisterRequest;
import com.edu.hcmuaf.springserver.auth.ResetPasswordRequest;
import com.edu.hcmuaf.springserver.entity.User;
import com.edu.hcmuaf.springserver.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("Login request received for username: {}", authenticationRequest.getUsername());
        AuthenticationResponse response = userService.authentication(authenticationRequest);
        if(response != null) {
            logger.info("User {} logged in successfully", authenticationRequest.getUsername());
        } else {
            logger.warn("Login failed for user {}", authenticationRequest.getUsername());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login_admin")
    public ResponseEntity<AuthenticationResponse> login_admin(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("Admin login request received for username: {}", authenticationRequest.getUsername());
        AuthenticationResponse response = userService.adminAuthentication(authenticationRequest);
        if(response != null) {
            logger.info("Admin {} logged in successfully", authenticationRequest.getUsername());
        } else {
            logger.warn("Admin login failed for user {}", authenticationRequest.getUsername());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Registration request received for email: {}", registerRequest.getEmail());
        AuthenticationResponse authenticationResponse = userService.register(registerRequest);
        if(authenticationResponse != null) {
            logger.info("User {} registered successfully", registerRequest.getEmail());
            return ResponseEntity.ok(authenticationResponse);
        }
        logger.warn("Registration failed for email: {}", registerRequest.getEmail());
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/login-google")
    public ResponseEntity<?> getUserInfo(@RequestParam("sub") String sub, @RequestParam("fullName") String fullname, @RequestParam("email") String email) {
        logger.info("Google login request received for email: {}", email);
        User user = userService.findUserByEmail(email);
        if(user != null) {
            logger.info("User {} found in database", email);
            return ResponseEntity.ok(userService.authenticationGoogle(user));
        }
        logger.info("User {} not found, registering new user", email);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(email);
        registerRequest.setPassword(sub);
        registerRequest.setEmail(email);
        registerRequest.setFullname(fullname);

        return register(registerRequest);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws MessagingException {
        logger.info("Password reset request received for email: {}", resetPasswordRequest.getEmail());
        AuthenticationResponse authenticationResponse = userService.resetPassword(resetPasswordRequest);
        if(authenticationResponse != null) {
            logger.info("Password reset successful for email: {}", resetPasswordRequest.getEmail());
            return ResponseEntity.ok(authenticationResponse);
        }
        logger.warn("Password reset failed for email: {}", resetPasswordRequest.getEmail());
        return ResponseEntity.badRequest().build();
    }

}
