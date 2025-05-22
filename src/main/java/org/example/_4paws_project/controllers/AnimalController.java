package org.example._4paws_project.controllers;

import java.util.List;

import org.example._4paws_project.DTO.AnimalDTO;
import org.example._4paws_project.DTO.PutForAdoptionDTO;
import org.example._4paws_project.models.Animal;
import org.example._4paws_project.services.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/animals")
@CrossOrigin(origins = "http://localhost:5173")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        return animalService.getAnimalById(id);
    }

    @PostMapping
    public ResponseEntity<Animal> createAnimal(
            @RequestHeader("Authorization") String authHeader,
            @ModelAttribute AnimalDTO animalDTO,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Animal createdAnimal = animalService.createAnimal(authHeader, animalDTO, image);
        return ResponseEntity.ok(createdAnimal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable Long id, @RequestBody Animal animal) {
        return animalService.updateAnimal(id, animal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        return animalService.deleteAnimal(id);
    }

    @PostMapping("/put-for-adoption")
    public ResponseEntity<Animal> putForAdoption(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PutForAdoptionDTO putForAdoptionDTO) {
        try {
            Animal animal = animalService.putForAdoption(authHeader, putForAdoptionDTO);
            return ResponseEntity.ok(animal);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
