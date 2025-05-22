package org.example._4paws_project.services;

import org.example._4paws_project.models.RescueGroup;
import org.example._4paws_project.repositories.RescueGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RescueGroupService {

    @Autowired
    private RescueGroupRepository rescueGroupRepository;

    public ResponseEntity<List<RescueGroup>> getAllRescueGroups() {
        List<RescueGroup> rescueGroups = rescueGroupRepository.findAll();
        return ResponseEntity.ok(rescueGroups);
    }

    public ResponseEntity<RescueGroup> getRescueGroupById(Long id) {
        Optional<RescueGroup> rescueGroup = rescueGroupRepository.findById(id);
        return rescueGroup.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<RescueGroup> addRescueGroup(RescueGroup rescueGroup) {
        RescueGroup savedRescueGroup = rescueGroupRepository.save(rescueGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRescueGroup);
    }

    public ResponseEntity<RescueGroup> updateRescueGroup(Long id, RescueGroup rescueGroupDetails) {
        if (!rescueGroupRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        RescueGroup updatedRescueGroup = rescueGroupRepository.findById(id).map(rescueGroup -> {
            rescueGroup.setName(rescueGroupDetails.getName());
            rescueGroup.setDescription(rescueGroupDetails.getDescription());
            return rescueGroupRepository.save(rescueGroup);
        }).orElseThrow(); // Nunca se ejecutará, porque ya verificamos la existencia del ID.

        return ResponseEntity.ok(updatedRescueGroup);
    }

    public ResponseEntity<Void> deleteRescueGroup(Long id) {
        if (!rescueGroupRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        rescueGroupRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
