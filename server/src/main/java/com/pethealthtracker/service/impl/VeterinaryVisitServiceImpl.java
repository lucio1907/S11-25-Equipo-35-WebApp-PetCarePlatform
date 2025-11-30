package com.pethealthtracker.service.impl;

import com.pethealthtracker.dto.veterinary_visits.VeterinaryVisitRequestDTO;
import com.pethealthtracker.dto.veterinary_visits.VeterinaryVisitResponseDTO;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.mapper.VeterinaryVisitMapper;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.model.VeterinaryVisit;
import com.pethealthtracker.repository.PetRepository;
import com.pethealthtracker.repository.VeterinaryVisitRepository;
import com.pethealthtracker.service.VeterinaryVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VeterinaryVisitServiceImpl implements VeterinaryVisitService {

    private final VeterinaryVisitRepository veterinaryVisitRepository;
    private final PetRepository petRepository;
    private final VeterinaryVisitMapper veterinaryVisitMapper;


    @Override
    @Transactional
    public VeterinaryVisitResponseDTO createVeterinaryVisit(Long userId, Long petId, VeterinaryVisitRequestDTO requestDTO) {
        // Verify pet exists and belongs to user
        Pet pet = petRepository.findByIdAndUserId(petId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId));

        // Map DTO to entity
        VeterinaryVisit visit = veterinaryVisitMapper.toEntity(requestDTO);
        visit.setPet(pet);

        // Save the visit
        VeterinaryVisit savedVisit = veterinaryVisitRepository.save(visit);

        // Map saved entity to response DTO
        return veterinaryVisitMapper.toDto(savedVisit);
    }

    @Override
    @Transactional(readOnly = true)
    public VeterinaryVisitResponseDTO getVeterinaryVisitById(Long userId, Long petId, Long visitId) {
        // Verify pet exists and belongs to user
        if (!petRepository.existsByIdAndUserId(petId, userId)) {
            throw new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId);
        }

        // Get the visit
        VeterinaryVisit visit = veterinaryVisitRepository.findByIdAndPetId(visitId, petId)
                .orElseThrow(() -> new ResourceNotFoundException("Visita veterinaria no encontrada con id: " + visitId));

        return veterinaryVisitMapper.toDto(visit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisitResponseDTO> getVeterinaryVisitsByPetId(Long userId, Long petId) {
        // Verify pet exists and belongs to user
        if (!petRepository.existsByIdAndUserId(petId, userId)) {
            throw new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId);
        }

        // Get all active visits for the pet
        return veterinaryVisitRepository.findByPetIdAndIsActiveTrue(petId).stream()
                .map(veterinaryVisitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VeterinaryVisitResponseDTO updateVeterinaryVisit(
            Long userId, Long petId, Long visitId, VeterinaryVisitRequestDTO requestDTO) {
        // Verify pet exists and belongs to user
        if (!petRepository.existsByIdAndUserId(petId, userId)) {
            throw new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId);
        }

        // Get existing visit
        VeterinaryVisit existingVisit = veterinaryVisitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visita veterinaria no encontrada con id: " + visitId));

        // Verify the visit belongs to the specified pet
        if (!existingVisit.getPet().getId().equals(petId)) {
            throw new ResourceNotFoundException("La visita no pertenece a la mascota especificada");
        }

        // Update fields from DTO
        veterinaryVisitMapper.updateFromDto(requestDTO, existingVisit);

        // Save updated visit
        VeterinaryVisit updatedVisit = veterinaryVisitRepository.save(existingVisit);

        return veterinaryVisitMapper.toDto(updatedVisit);
    }

    @Override
    @Transactional
    public void deleteVeterinaryVisit(Long userId, Long petId, Long visitId) {
        // Verify pet exists and belongs to user
        if (!petRepository.existsByIdAndUserId(petId, userId)) {
            throw new ResourceNotFoundException("Mascota no encontrada con id: " + petId + " para el usuario: " + userId);
        }

        // Get the visit
        VeterinaryVisit visit = veterinaryVisitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visita veterinaria no encontrada con id: " + visitId));

        // Verify the visit belongs to the specified pet
        if (!visit.getPet().getId().equals(petId)) {
            throw new ResourceNotFoundException("La visita no pertenece a la mascota especificada");
        }

        // Soft delete
        visit.setIsActive(false);
        veterinaryVisitRepository.save(visit);
    }
}
