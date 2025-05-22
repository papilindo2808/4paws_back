package org.example._4paws_project.services;

import org.example._4paws_project.models.Community;
import org.example._4paws_project.models.User;
import org.example._4paws_project.repositories.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {
    
    @Autowired
    private CommunityRepository communityRepository;

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public Optional<Community> getCommunityById(Long id) {
        return communityRepository.findById(id);
    }

    public List<Community> getCommunitiesByCategory(String category) {
        return communityRepository.findByCategory(category);
    }

    public List<Community> searchCommunities(String name) {
        return communityRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Community createCommunity(Community community) {
        return communityRepository.save(community);
    }

    @Transactional
    public Community updateCommunity(Long id, Community communityDetails) {
        Community community = communityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        
        community.setName(communityDetails.getName());
        community.setDescription(communityDetails.getDescription());
        community.setCategory(communityDetails.getCategory());
        community.setImageUrl(communityDetails.getImageUrl());
        
        return communityRepository.save(community);
    }

    @Transactional
    public void deleteCommunity(Long id) {
        communityRepository.deleteById(id);
    }

    @Transactional
    public Community followCommunity(Long communityId, User user) {
        Community community = communityRepository.findById(communityId)
            .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        
        if (!community.getFollowers().contains(user)) {
            community.getFollowers().add(user);
            community.setMembers(community.getMembers() + 1);
        }
        
        return communityRepository.save(community);
    }

    @Transactional
    public Community unfollowCommunity(Long communityId, User user) {
        Community community = communityRepository.findById(communityId)
            .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        
        if (community.getFollowers().contains(user)) {
            community.getFollowers().remove(user);
            community.setMembers(community.getMembers() - 1);
        }
        
        return communityRepository.save(community);
    }
} 