package org.example._4paws_project.controllers;

import org.example._4paws_project.models.Community;
import org.example._4paws_project.models.User;
import org.example._4paws_project.services.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/communities")
@CrossOrigin(origins = "*")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping
    public List<Community> getAllCommunities() {
        return communityService.getAllCommunities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunityById(@PathVariable Long id) {
        return communityService.getCommunityById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public List<Community> getCommunitiesByCategory(@PathVariable String category) {
        return communityService.getCommunitiesByCategory(category);
    }

    @GetMapping("/search")
    public List<Community> searchCommunities(@RequestParam String name) {
        return communityService.searchCommunities(name);
    }

    @PostMapping
    public Community createCommunity(@RequestBody Community community, @AuthenticationPrincipal User user) {
        if (community.getFollowers() == null) {
            community.setFollowers(new ArrayList<>());
        }
        community.setMembers(1);
        community.getFollowers().add(user);
        return communityService.createCommunity(community);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Community> updateCommunity(@PathVariable Long id, @RequestBody Community communityDetails) {
        try {
            Community updatedCommunity = communityService.updateCommunity(id, communityDetails);
            return ResponseEntity.ok(updatedCommunity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {
        try {
            communityService.deleteCommunity(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<Community> followCommunity(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            Community community = communityService.followCommunity(id, user);
            return ResponseEntity.ok(community);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<Community> unfollowCommunity(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            Community community = communityService.unfollowCommunity(id, user);
            return ResponseEntity.ok(community);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 
