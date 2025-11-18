package com.pethealthtracker.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

import com.pethealthtracker.dto.pets.PetDTO;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.model.User;
import com.pethealthtracker.repository.PetRepository;
import com.pethealthtracker.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    // 1. Crear una nueva mascota
    @Transactional
    public PetDTO createNewPet(PetDTO petDTO) {
        // 1. Buscar el usuario propietario de la mascota
        User owner = userRepository.findById(petDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Mapear DTO a entidad Pet
        Pet pet = mapDtoToEntity(petDTO, owner);
        
        // 3. Lógica de negocio: Calcular la edad (Opcional, pero recomendado)
        calculateAge(pet);

        // 4. Guardar la mascota en la base de datos
        petRepository.save(pet);

        // 5. Devolver el DTO (o un DTO de Respuesta si lo creas)
        return petDTO; 
    }

    private Pet mapDtoToEntity(PetDTO petDTO, User owner) {
        return Pet.builder()
                .user(owner)
                .name(petDTO.getName())
                .species(petDTO.getSpecies())
                .breed(petDTO.getBreed())
                .dateOfBirth(petDTO.getDateOfBirth())
                .weight(petDTO.getWeight())
                .weightUnit(petDTO.getWeightUnit())
                .profilePictureUrl(petDTO.getProfilePictureUrl())
                .microchipNumber(petDTO.getMicrochipNumber())
                .color(petDTO.getColor())
                .gender(petDTO.getGender())
                .healthNotes(petDTO.getHealthNotes())
                .isActive(true)
                .build();
    }
    
    private void calculateAge(Pet pet) {
        if (pet.getDateOfBirth() != null) {
            Period age = Period.between(pet.getDateOfBirth(), LocalDate.now());
            pet.setAgeYears(age.getYears());
            pet.setAgeMonths(age.getMonths());
        }
    }
}