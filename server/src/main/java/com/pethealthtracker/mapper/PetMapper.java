package com.pethealthtracker.mapper;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.pethealthtracker.dto.pets.PetRequestDTO;
import com.pethealthtracker.dto.pets.PetResponseDTO;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.model.User;

@Component
public class PetMapper {
    public Pet toEntity(PetRequestDTO dto, User owner) {
        return Pet.builder()
                .user(owner)
                .name(dto.getName())
                .species(dto.getSpecies())
                .breed(dto.getBreed())
                .dateOfBirth(dto.getDateOfBirth())
                .weight(dto.getWeight())
                .weightUnit(dto.getWeightUnit())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .microchipNumber(dto.getMicrochipNumber())
                .color(dto.getColor())
                .gender(dto.getGender())
                .healthNotes(dto.getHealthNotes())
                .isActive(true)
                .build();
    }

    public PetResponseDTO toResponseDTO(Pet pet) {
        int ageYears = 0;
        int ageMonths = 0;

        if (pet.getDateOfBirth() != null) {
            Period period = Period.between(pet.getDateOfBirth(), LocalDate.now());
            ageYears = period.getYears();
            ageMonths = period.getMonths();
        }

        return PetResponseDTO.builder()
                .id(pet.getId())
                .userId(pet.getUser().getId())
                .name(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .dateOfBirth(pet.getDateOfBirth())
                .ageYears(ageYears)
                .ageMonths(ageMonths)
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
}
