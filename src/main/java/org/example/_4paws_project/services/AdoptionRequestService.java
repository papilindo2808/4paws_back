package org.example._4paws_project.services;

import org.example._4paws_project.DTO.AdoptionDTO;
import org.example._4paws_project.models.AdoptionRequest;
import org.example._4paws_project.models.Animal;
import org.example._4paws_project.models.User;
import org.example._4paws_project.repositories.AdoptionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdoptionRequestService {

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    public List<AdoptionRequest> getAllAdoptionRequests() {
        return adoptionRequestRepository.findAll();
    }

    public Optional<AdoptionRequest> getAdoptionRequestById(Long id) {
        return adoptionRequestRepository.findById(id);
    }


    public AdoptionDTO addAdoptionRequest(AdoptionDTO adoptionDTO) {
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setStatus(adoptionDTO.getStatus());
        adoptionRequest.setUser(new User(adoptionDTO.getUserId()));
        adoptionRequest.setAnimal(new Animal(adoptionDTO.getAnimalId()));
        AdoptionRequest savedRequest = adoptionRequestRepository.save(adoptionRequest);
        return convertToDTO(savedRequest);
    }

    public Optional<AdoptionDTO> updateAdoptionRequest(Long id, AdoptionDTO adoptionDTO) {
        return adoptionRequestRepository.findById(id).map(adoptionRequest -> {
            adoptionRequest.setStatus(adoptionDTO.getStatus());
            adoptionRequest.setUser(new User(adoptionDTO.getUserId()));
            adoptionRequest.setAnimal(new Animal(adoptionDTO.getAnimalId()));
            AdoptionRequest updatedRequest = adoptionRequestRepository.save(adoptionRequest);
            return convertToDTO(updatedRequest);
        });
    }

    public boolean deleteAdoptionRequest(Long id) {
        if (adoptionRequestRepository.existsById(id)) {
            adoptionRequestRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private AdoptionDTO convertToDTO(AdoptionRequest adoptionRequest) {
        AdoptionDTO adoptionDTO = new AdoptionDTO();
        adoptionDTO.setId(adoptionRequest.getId());
        adoptionDTO.setStatus(adoptionRequest.getStatus());
        adoptionDTO.setUserId(adoptionRequest.getUser().getId());
        adoptionDTO.setAnimalId(adoptionRequest.getAnimal().getId());
        return adoptionDTO;
    }
}