package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.auth.AuthenticationRequest;
import com.edu.hcmuaf.springserver.auth.AuthenticationResponse;
import com.edu.hcmuaf.springserver.auth.RegisterRequest;
import com.edu.hcmuaf.springserver.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.authentication(authenticationRequest));
    }

    @PostMapping("/login_admin")
    public ResponseEntity<AuthenticationResponse> login_admin(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.adminAuthentication(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        AuthenticationResponse authenticationResponse = userService.register(registerRequest);
        if(authenticationResponse != null) {
            return ResponseEntity.ok(authenticationResponse);
        }
        return ResponseEntity.badRequest().build();
    }
}
