package org.example._4paws_project.controllers;

import org.example._4paws_project.models.RescueGroup;
import org.example._4paws_project.services.RescueGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rescue-groups")
@CrossOrigin(origins = "http://localhost:5173")
public class RescueGroupController {

    @Autowired
    private RescueGroupService rescueGroupService;

    @GetMapping
    public ResponseEntity<List<RescueGroup>> getAllRescueGroups() {
        return rescueGroupService.getAllRescueGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RescueGroup> getRescueGroupById(@PathVariable Long id) {
        return rescueGroupService.getRescueGroupById(id);
    }

    @PostMapping
    public ResponseEntity<RescueGroup> addRescueGroup(@RequestBody RescueGroup rescueGroup) {
        return rescueGroupService.addRescueGroup(rescueGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RescueGroup> updateRescueGroup(@PathVariable Long id, @RequestBody RescueGroup rescueGroupDetails) {
    return rescueGroupService.updateRescueGroup(id, rescueGroupDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRescueGroup(@PathVariable Long id) {
            return ResponseEntity.notFound().build();
        }
    }
