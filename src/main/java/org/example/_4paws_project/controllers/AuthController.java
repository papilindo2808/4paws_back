package org.example._4paws_project.controllers;

import org.example._4paws_project.DTO.LoginDTO;
import org.example._4paws_project.models.User;
import org.example._4paws_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO user){
        return userService.login(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        return userService.getCurrentUser(authHeader);
    }
}
