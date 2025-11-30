package com.pethealthtracker.repository;

import com.pethealthtracker.model.VeterinaryVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinaryVisitRepository extends JpaRepository<VeterinaryVisit, Long> {
    
    List<VeterinaryVisit> findByPetIdAndIsActiveTrue(Long petId);
    
    Optional<VeterinaryVisit> findByIdAndPetId(Long id, Long petId);
    
    boolean existsByIdAndPetId(Long id, Long petId);
}
