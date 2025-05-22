// src/main/java/org/example/_4paws_project/services/AnimalService.java
package org.example._4paws_project.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.annotation.PostConstruct;

import org.example._4paws_project.DTO.AnimalDTO;
import org.example._4paws_project.DTO.PutForAdoptionDTO;
import org.example._4paws_project.models.Animal;
import org.example._4paws_project.models.User;
import org.example._4paws_project.repositories.AnimalRepository;
import org.example._4paws_project.repositories.UserRepository;
import org.example._4paws_project.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private static final String IMAGE_UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @PostConstruct
    public void init() {
        File uploadDir = new File(IMAGE_UPLOAD_DIR);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new RuntimeException("No se pudo crear el directorio de uploads");
            }
        }
    }

    public AnimalService(AnimalRepository animalRepository, JwtUtil jwtUtil, UserRepository userRepository) {
        this.animalRepository = animalRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<Animal>> getAllAnimals() {
        List<Animal> animals = animalRepository.findAll();
        System.out.println("Total de animales encontrados: " + animals.size());
        animals.forEach(animal -> System.out.println("Animal ID: " + animal.getId() + ", Nombre: " + animal.getName()));
        return ResponseEntity.ok(animals);
    }

    public ResponseEntity<Animal> getAnimalById(Long id) {
        Optional<Animal> animal = animalRepository.findById(id);
        return animal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public Animal createAnimal(String authHeader, AnimalDTO animalDTO, MultipartFile image) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token no proporcionado");
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Token inválido");
        }

        // Obtener el usuario actual
        User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File destFile = new File(IMAGE_UPLOAD_DIR + fileName);
                image.transferTo(destFile);
                imageUrl = "/uploads/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
            }
        }

        Animal animal = new Animal();
        animal.setName(animalDTO.getName());
        animal.setSpecies(animalDTO.getSpecies());
        animal.setBreed(animalDTO.getBreed());
        animal.setDescription(animalDTO.getDescription());
        animal.setBirthDate(LocalDate.parse(animalDTO.getBirthDate()));
        animal.setGender(animalDTO.getGender());
        animal.setAdopted(animalDTO.isAdopted());
        animal.setImagenUrl(imageUrl != null ? imageUrl : animalDTO.getImagenUrl());
        animal.setSize(animalDTO.getSize());
        animal.setUser(currentUser); // Asociar el animal con el usuario actual

        return animalRepository.save(animal);
    }

    public ResponseEntity<Animal> updateAnimal(Long id, Animal animalDetails) {
        return animalRepository.findById(id)
                .map(animal -> {
                    animal.setName(animalDetails.getName());
                    animal.setSpecies(animalDetails.getSpecies());
                    animal.setBreed(animalDetails.getBreed());
                    animal.setBirthDate(animalDetails.getBirthDate());
                    animal.setAdopted(animalDetails.isAdopted());
                    animal.setImagenUrl(animalDetails.getImagenUrl());
                    animal.setSize(animalDetails.getSize());
                    Animal updatedAnimal = animalRepository.save(animal);
                    return ResponseEntity.ok(updatedAnimal);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deleteAnimal(Long id) {
        if (animalRepository.existsById(id)) {
            animalRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Animal putForAdoption(String authHeader, PutForAdoptionDTO putForAdoptionDTO) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token no proporcionado");
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Token inválido");
        }

        Animal animal = new Animal();
        AnimalDTO animalDTO = putForAdoptionDTO.getAnimal();
        
        animal.setName(animalDTO.getName());
        animal.setSpecies(animalDTO.getSpecies());
        animal.setBreed(animalDTO.getBreed());
        animal.setDescription(animalDTO.getDescription());
        animal.setBirthDate(LocalDate.parse(animalDTO.getBirthDate()));
        animal.setGender(animalDTO.getGender());
        animal.setAdopted(false); // Siempre falso al poner en adopción
        animal.setImagenUrl(animalDTO.getImagenUrl());
        animal.setSize(animalDTO.getSize());
        
        // Aquí podrías agregar lógica adicional, como validar el usuario
        // o agregar el animal a un grupo de rescate específico

        return animalRepository.save(animal);
    }
}