package com.pethealthtracker.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pethealthtracker.dto.pets.PetRequestDTO;
import com.pethealthtracker.dto.pets.PetResponseDTO;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.model.User;
import com.pethealthtracker.repository.PetRepository;
import com.pethealthtracker.repository.UserRepository;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    // -----------------------------------------------------
    // 1. CREAR MASCOTA (POST)
    // -----------------------------------------------------
    @Transactional
    public PetResponseDTO createPet(Long userId, PetRequestDTO petRequestDTO) {
        // 1. Buscar el usuario propietario (validación de FK)
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));

        // 2. Verificar si la mascota ya existe
        if (petRepository.findByNameAndUserId(petRequestDTO.getName(), userId).isPresent()) {
            throw new ResourceNotFoundException("Mascota", "nombre", petRequestDTO.getName());
        }

        // 3. Mapear DTO a entidad Pet
        Pet pet = PetService.mapDtoToEntity(petRequestDTO, owner);

        // 4. Lógica de negocio: Calcular la edad
        PetService.calculateAge(pet);

        // 5. Guardar la mascota en la base de datos
        Pet savedPet = petRepository.save(pet);

        // 6. Devolver el DTO de Respuesta
        return PetService.mapToResponseDTO(savedPet);
    }

    // -----------------------------------------------------
    // 2. OBTENER MASCOTA POR NOMBRE Y USUARIO (GET)
    // -----------------------------------------------------
    @Transactional(readOnly = true)
    public PetResponseDTO getPetByNameAndUserId(String name, Long userId) {
        // Usa la consulta del repositorio que verifica el nombre Y el usuario
        Pet pet = petRepository.findByNameAndUserId(name, userId)
            .orElseThrow(() -> new ResourceNotFoundException("La mascota no esta registrada para este usuario."));
            
        return PetService.mapToResponseDTO(pet);
    }

    // -----------------------------------------------------
    // 3. OBTENER TODAS LAS MASCOTAS DE UN USUARIO (GET)
    // -----------------------------------------------------
    @Transactional(readOnly = true)
    public List<PetResponseDTO> getPetsByUserId(Long userId) {
        // Busca todas las mascotas del usuario
        List<Pet> pets = petRepository.findByUserId(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")); 

        if (pets.isEmpty()) {
            throw new ResourceNotFoundException("El usuario no tiene mascotas registradas.");
        }
        
        // Mapear la lista de entidades a la lista de DTOs
        return pets.stream()
                .map(PetService::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------
    // 4. ACTUALIZAR MASCOTA (PUT)
    // -----------------------------------------------------
    @Transactional
    public PetResponseDTO updatePet(Long petId, Long userId, PetRequestDTO requestDTO) {
        // 1. Verificar existencia y propiedad
        Pet existingPet = petRepository.findByIdAndUserId(petId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mascota", "id", petId));

        // 2. Actualizar campos (Usamos Setters de Lombok)
        existingPet.setName(requestDTO.getName());
        existingPet.setSpecies(requestDTO.getSpecies());
        existingPet.setBreed(requestDTO.getBreed());
        existingPet.setDateOfBirth(requestDTO.getDateOfBirth());
        existingPet.setWeight(requestDTO.getWeight());
        existingPet.setWeightUnit(requestDTO.getWeightUnit());
        existingPet.setProfilePictureUrl(requestDTO.getProfilePictureUrl());
        existingPet.setMicrochipNumber(requestDTO.getMicrochipNumber());
        existingPet.setColor(requestDTO.getColor());
        existingPet.setGender(requestDTO.getGender());
        existingPet.setHealthNotes(requestDTO.getHealthNotes());
        // NO se permite cambiar el userId ni isActive en un PUT normal.
        
        // 3. Recalcular la edad
        PetService.calculateAge(existingPet);

        // 4. Guardar
        Pet updatedPet = petRepository.save(existingPet);
        
        // 5. Devolver DTO
        return PetService.mapToResponseDTO(updatedPet);
    }

    // -----------------------------------------------------
    // 5. ELIMINAR MASCOTA (DESACTIVACIÓN LÓGICA)
    // -----------------------------------------------------
    @Transactional
    public void softDeletePet(Long petId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario", "id", userId);
        }

        Pet existingPet = petRepository.findByIdAndUserId(petId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mascota", "id", petId));
        
        if (!existingPet.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("La mascota no pertenece al usuario especificado.");
        }

        existingPet.setIsActive(false);

        petRepository.save(existingPet);
    }

    // --- MAPPERS Y LÓGICA ESTÁTICA ---
    
    // El método de mapeo del DTO de petición a la entidad
    private static Pet mapDtoToEntity(PetRequestDTO petRequestDTO, User owner) {
        return Pet.builder()
            .user(owner)
            .name(petRequestDTO.getName())
            .species(petRequestDTO.getSpecies())
            .breed(petRequestDTO.getBreed())
            .dateOfBirth(petRequestDTO.getDateOfBirth())
            .weight(petRequestDTO.getWeight())
            .weightUnit(petRequestDTO.getWeightUnit())
            .profilePictureUrl(petRequestDTO.getProfilePictureUrl())
            .microchipNumber(petRequestDTO.getMicrochipNumber())
            .color(petRequestDTO.getColor())
            .gender(petRequestDTO.getGender())
            .healthNotes(petRequestDTO.getHealthNotes())
            .isActive(true)
            .build();
    }

    // El método de mapeo de la entidad al DTO de respuesta
    private static PetResponseDTO mapToResponseDTO(Pet pet) {
        PetService.calculateAge(pet);

        return PetResponseDTO.builder()
            .id(pet.getId())
            .userId(pet.getUser().getId())
            .name(pet.getName())
            .species(pet.getSpecies())
            .breed(pet.getBreed())
            .dateOfBirth(pet.getDateOfBirth())
            .ageYears(pet.getAgeYears())
            .ageMonths(pet.getAgeMonths())
            .weight(pet.getWeight())
            .weightUnit(pet.getWeightUnit())
            .profilePictureUrl(pet.getProfilePictureUrl())
            .microchipNumber(pet.getMicrochipNumber())
            .color(pet.getColor())
            .gender(pet.getGender())
            .healthNotes(pet.getHealthNotes())
            .isActive(pet.getIsActive())
            .createdAt(pet.getCreatedAt())
            .updatedAt(pet.getUpdatedAt())
            .build();
    }

    // Lógica para calcular la edad
    private static void calculateAge(Pet pet) {
        if (pet.getDateOfBirth() != null) {
            Period age = Period.between(pet.getDateOfBirth(), LocalDate.now());
            pet.setAgeYears(age.getYears());
            pet.setAgeMonths(age.getMonths());
        }
    }
}