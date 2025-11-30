package com.pethealthtracker.repository;

import com.pethealthtracker.model.DewormingTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DewormingTreatmentRepository extends JpaRepository<DewormingTreatment, Long> {
    
    // Find all deworming treatments for a specific pet
    List<DewormingTreatment> findByPetId(Long petId);
    
    // Find active deworming treatments for a specific pet
    List<DewormingTreatment> findByPetIdAndIsActiveTrue(Long petId);
    
    // Find deworming treatments by product name (case-insensitive)
    List<DewormingTreatment> findByProductNameContainingIgnoreCase(String productName);
    
    // Find a specific deworming treatment by ID and pet ID
    Optional<DewormingTreatment> findByIdAndPetId(Long id, Long petId);
    
    // Delete all deworming treatments for a specific pet
    void deleteByPetId(Long petId);
}
