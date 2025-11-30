package com.pethealthtracker.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pethealthtracker.dto.pets.PetRequestDTO;
import com.pethealthtracker.dto.pets.PetResponseDTO;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.mapper.PetMapper;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.model.User;
import com.pethealthtracker.repository.PetRepository;
import com.pethealthtracker.repository.UserRepository;
import com.pethealthtracker.service.PetService;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    public PetServiceImpl(PetRepository petRepository, UserRepository userRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petMapper = petMapper;
    }

    // -----------------------------------------------------
    // 1. CREAR MASCOTA (POST)
    // -----------------------------------------------------
    @Override
    @Transactional
    public PetResponseDTO createPet(Long userId, PetRequestDTO petRequestDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));

        if (petRepository.findByNameAndUserId(petRequestDTO.getName(), userId).isPresent()) {
            throw new ResourceNotFoundException("La mascota ya esta registrada para este usuario.");
        }

        if (petRequestDTO.getMicrochipNumber() != null &&
                petRepository.findByMicrochipNumber(petRequestDTO.getMicrochipNumber()).isPresent()) {
            throw new ResourceNotFoundException("El número de microchip ya está registrado para otra mascota.");
        }

        // Llamamos al método interno directamente (sin PetService.)
        Pet pet = petMapper.toEntity(petRequestDTO, owner);


        Pet savedPet = petRepository.save(pet);

        return petMapper.toResponseDTO(savedPet);
    }

    // -----------------------------------------------------
    // 2. OBTENER MASCOTA POR NOMBRE Y USUARIO (GET)
    // -----------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public PetResponseDTO getPetByNameAndUserId(String name, Long userId) {
        Pet pet = petRepository.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new ResourceNotFoundException("La mascota no esta registrada para este usuario."));

        return petMapper.toResponseDTO(pet);
    }

    // -----------------------------------------------------
    // 3. OBTENER TODAS LAS MASCOTAS DE UN USUARIO (GET)
    // -----------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<PetResponseDTO> getPetsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario", "id", userId);
        }

        List<Pet> pets = petRepository.findByUserId(userId);

        if (pets.isEmpty()) {
            throw new ResourceNotFoundException("El usuario no tiene mascotas registradas.");
        }

        return pets.stream()
                .map(petMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------
    // 4. ACTUALIZAR MASCOTA (PUT)
    // -----------------------------------------------------
    @Override
    @Transactional
    public PetResponseDTO updatePet(Long petId, Long userId, PetRequestDTO requestDTO) {
        Pet existingPet = petRepository.findByIdAndUserId(petId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota", "id", petId));

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


        Pet updatedPet = petRepository.save(existingPet);

        return petMapper.toResponseDTO(updatedPet);
    }

    // -----------------------------------------------------
    // 5. ELIMINAR MASCOTA (DESACTIVACIÓN LÓGICA)
    // -----------------------------------------------------
    @Override
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
}