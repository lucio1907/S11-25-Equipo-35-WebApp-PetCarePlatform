package com.pethealthtracker.repository;

import com.pethealthtracker.model.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    
    List<Vaccination> findByPetId(Long petId);
    
    Optional<Vaccination> findByIdAndPetId(Long id, Long petId);
    
    List<Vaccination> findByPetIdAndIsActiveTrue(Long petId);
    
    boolean existsByIdAndPetId(Long id, Long petId);
}
