package com.pethealthtracker.service;

import com.pethealthtracker.dto.pets.PetRequestDTO;
import com.pethealthtracker.dto.pets.PetResponseDTO;
import java.util.List;

public interface PetService {
    
    PetResponseDTO createPet(Long userId, PetRequestDTO petRequestDTO);
    
    PetResponseDTO getPetByNameAndUserId(String name, Long userId);
    
    List<PetResponseDTO> getPetsByUserId(Long userId);
    
    PetResponseDTO updatePet(Long petId, Long userId, PetRequestDTO requestDTO);
    
    void softDeletePet(Long petId, Long userId);
}