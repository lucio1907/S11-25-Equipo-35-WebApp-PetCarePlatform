package com.pethealthtracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pethealthtracker.model.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long>{
    
    // 1. Encontrar medicamentos por ID de mascota
    List<Medication> findByPetId(Long petId);
    // 2. Encontrar medicamentos activos por ID de mascota
    List<Medication> findByPetIdAndIsActiveTrue(Long petId);

    // 3. Encontrar medicamentos por nombre (Búsqueda parcial)
    List<Medication> findByMedicationNameContainingIgnoreCase(String medicationName);

    // 4. Encontrar medicamentos activos por nombre
    List<Medication> findByMedicationNameContainingIgnoreCaseAndIsActiveTrue(String medicationName);

    // 5. Eliminar (Físicamente) medicamentos por ID de mascota
    void deleteByPetId(Long petId);
    
    // Opcional: Buscar un medicamento específico y validar que pertenezca a una mascota
    Optional<Medication> findByIdAndPetId(Long id, Long petId);
}