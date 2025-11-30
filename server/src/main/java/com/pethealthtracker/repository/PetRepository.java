package com.pethealthtracker.repository;

import com.pethealthtracker.model.Pet;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
   Interfaz de repositorio para la entidad Pet.
*/

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    
    // 1. Encontrar mascotas por ID de usuario
    List<Pet> findByUserId(Long userId);

    // 2. Encontrar todas las mascotas activas por ID de usuario
    List<Pet> findByUserIdAndIsActiveTrue(Long userId);

    // 3. Encontrar mascota por nombre e ID de usuario
    Optional<Pet> findByNameAndUserId(String name, Long userId);

    // 4. Contar mascotas por usuario
    long countByUserId(Long userId);

    // 5. Encontrar mascotas por especie
    List<Pet> findBySpecies(String species);

    // 6. Encontrar mascota por ID de Mascota e ID de Usuario (Para verificación de propiedad)
    Optional<Pet> findByIdAndUserId(Long petId, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
    Optional<Pet> findByMicrochipNumber(String microchipNumber);
}