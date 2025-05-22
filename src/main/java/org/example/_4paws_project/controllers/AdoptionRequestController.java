package org.example._4paws_project.controllers;

import org.example._4paws_project.DTO.AdoptionDTO;
import org.example._4paws_project.models.AdoptionRequest;
import org.example._4paws_project.services.AdoptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adoption-requests")
@CrossOrigin(origins = "http://localhost:5173")
public class AdoptionRequestController {

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @GetMapping
    public List<AdoptionRequest> getAllAdoptionRequests() {
        return adoptionRequestService.getAllAdoptionRequests();
    }

    @GetMapping("/{id}")
    public Optional<AdoptionRequest> getAdoptionRequestById(@PathVariable Long id) {
        return adoptionRequestService.getAdoptionRequestById(id);
    }

    @PostMapping
    public AdoptionDTO createAdoptionRequest(@RequestBody AdoptionDTO adoptionDTO) {
        return adoptionRequestService.addAdoptionRequest(adoptionDTO);
    }

    @PutMapping("/{id}")
    public Optional<AdoptionDTO> updateAdoptionRequest(@PathVariable Long id, @RequestBody AdoptionDTO adoptionDTO) {
        return adoptionRequestService.updateAdoptionRequest(id,adoptionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoptionRequest(@PathVariable Long id) {
        if (adoptionRequestService.deleteAdoptionRequest(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}