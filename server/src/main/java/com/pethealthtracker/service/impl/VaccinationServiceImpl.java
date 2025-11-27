package com.pethealthtracker.service.impl;

import com.pethealthtracker.dto.vaccinations.VaccinationRequestDTO;
import com.pethealthtracker.dto.vaccinations.VaccinationResponseDTO;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.mapper.VaccinationMapper;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.model.Vaccination;
import com.pethealthtracker.repository.PetRepository;
import com.pethealthtracker.repository.VaccinationRepository;
import com.pethealthtracker.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationServiceImpl implements VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final PetRepository petRepository;
    private final VaccinationMapper vaccinationMapper;

    @Override
    @Transactional
    public VaccinationResponseDTO createVaccination(Long userId, Long petId, VaccinationRequestDTO requestDTO) {
        // Verificar que la mascota exista y pertenezca al usuario
        Pet pet = petRepository.findByIdAndUserId(petId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId));

        // Mapear DTO a entidad
        Vaccination vaccination = vaccinationMapper.toEntity(requestDTO);
        vaccination.setPet(pet);

        // Guardar la vacunación
        Vaccination savedVaccination = vaccinationRepository.save(vaccination);

        // Mapear entidad guardada a DTO de respuesta
        return vaccinationMapper.toDto(savedVaccination);
    }

    @Override
    @Transactional(readOnly = true)
    public VaccinationResponseDTO getVaccinationById(Long userId, Long petId, Long vaccinationId) {
        // Verificar que la mascota exista y pertenezca al usuario
        if (!petRepository.existsByIdAndUserId(petId, userId)) {
            throw new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId);
        }

        // Obtener la vacunación
        Vaccination vaccination = vaccinationRepository.findByIdAndPetId(vaccinationId, petId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacunación no encontrada con id: " + vaccinationId));

        return vaccinationMapper.toDto(vaccination);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationResponseDTO> getVaccinationsByPetId(Long userId, Long petId) {
        // Verificar que la mascota exista y pertenezca al usuario
        if (!petRepository.existsByIdAndUserId(petId, userId)) {
            throw new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId);
        }

        // Obtener todas las vacunaciones activas de la mascota
        return vaccinationRepository.findByPetIdAndIsActiveTrue(petId).stream()
                .map(vaccinationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VaccinationResponseDTO updateVaccination(Long userId, Long petId, Long vaccinationId, VaccinationRequestDTO requestDTO) {
        // Verificar que la mascota exista y pertenezca al usuario
        if (!petRepository.existsByIdAndUserId(petId, userId)) {
            throw new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId);
        }

        // Obtener la vacunación existente
        Vaccination existingVaccination = vaccinationRepository.findByIdAndPetId(vaccinationId, petId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacunación no encontrada con id: " + vaccinationId));

        // Actualizar campos de la vacunación
        existingVaccination.setVaccineName(requestDTO.getVaccineName());
        existingVaccination.setManufacturer(requestDTO.getManufacturer());
        existingVaccination.setBatchNumber(requestDTO.getBatchNumber());
        existingVaccination.setAdministrationDate(requestDTO.getAdministrationDate());
        existingVaccination.setExpirationDate(requestDTO.getExpirationDate());
        existingVaccination.setNextDoseDate(requestDTO.getNextDoseDate());
        existingVaccination.setAdministeredBy(requestDTO.getAdministeredBy());
        existingVaccination.setNotes(requestDTO.getNotes());

        // Guardar los cambios
        Vaccination updatedVaccination = vaccinationRepository.save(existingVaccination);

        return vaccinationMapper.toDto(updatedVaccination);
    }

    @Override
    @Transactional
    public void deleteVaccination(Long userId, Long petId, Long vaccinationId) {
        // Verificar que la mascota exista y pertenezca al usuario
        if (!petRepository.existsByIdAndUserId(petId, userId)) {
            throw new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId);
        }

        // Verificar que la vacunación exista y pertenezca a la mascota
        Vaccination vaccination = vaccinationRepository.findByIdAndPetId(vaccinationId, petId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacunación no encontrada con id: " + vaccinationId));

        // Eliminación lógica
        vaccination.setIsActive(false);
        vaccinationRepository.save(vaccination);
    }
}
