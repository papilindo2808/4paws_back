package org.example._4paws_project.repositories;

import org.example._4paws_project.models.RescueGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RescueGroupRepository extends JpaRepository<RescueGroup, Long> {
}
