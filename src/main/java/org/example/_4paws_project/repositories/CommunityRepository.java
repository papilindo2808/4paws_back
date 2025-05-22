package org.example._4paws_project.repositories;

import org.example._4paws_project.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findByCategory(String category);
    List<Community> findByNameContainingIgnoreCase(String name);
} 