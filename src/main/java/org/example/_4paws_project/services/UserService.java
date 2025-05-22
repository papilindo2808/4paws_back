package org.example._4paws_project.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example._4paws_project.DTO.AuthToken;
import org.example._4paws_project.DTO.LoginDTO;
import org.example._4paws_project.models.ErrorResponse;
import org.example._4paws_project.models.User;
import org.example._4paws_project.repositories.UserRepository;
import org.example._4paws_project.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public ResponseEntity<?> register(User usuario) {
        // Verificación de usuario y email
        if (userRepository.findByUsername(usuario.getUsername()).isPresent()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "USERNAME_EXISTS", "El usuario ya existe");
        }
        if (userRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "EMAIL_EXISTS", "El correo ya ha sido registrado");
        }
        usuario.setRole(User.Role.ADOPTER);

        // Encriptación de la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        userRepository.save(usuario);

        // Usando Map para la respuesta JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario registrado exitosamente");

        return ResponseEntity.ok(response);
    }



    public ResponseEntity<?> login(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow();
            System.out.println("User found: " + user);
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(new AuthToken(token, user));
        } catch (Exception e) {
            // Agrega más detalles en el log para depurar
            e.printStackTrace(); // Imprime el stack trace completo en los logs para depuración
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Credenciales incorrectas");
        }
    }

    public ResponseEntity<?> getCurrentUser(String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return buildErrorResponse(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Token no proporcionado o inválido");
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username == null || username.isEmpty()) {
                return buildErrorResponse(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Token inválido");
            }

            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            return ResponseEntity.ok(new AuthToken(token, user));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "AUTH_ERROR", "Error de autenticación");
        }
    }

    private ResponseEntity<?> buildErrorResponse(HttpStatus status, String errorCode, String message) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return ResponseEntity.status(status).body(errorResponse);
    }


}
