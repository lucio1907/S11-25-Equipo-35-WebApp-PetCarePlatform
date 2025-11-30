package com.pethealthtracker.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pethealthtracker.dto.medications.MedicationRequestDTO;
import com.pethealthtracker.dto.medications.MedicationResponseDTO;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.mapper.MedicationMapper;
import com.pethealthtracker.model.Medication;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.repository.MedicationRepository;
import com.pethealthtracker.repository.PetRepository;
import com.pethealthtracker.service.MedicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final PetRepository petRepository;
    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;
    // -----------------------------------------------------
    // 1. CREAR MEDICAMENTO
    // -----------------------------------------------------
    
    @Transactional
    public MedicationResponseDTO createMedication(MedicationRequestDTO requestDTO) {
        // 1. Validar que la mascota exista
        Pet pet = petRepository.findById(requestDTO.getPetId()).orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        // 2. Mapear DTO a entidad
        Medication medication = medicationMapper.toEntity(requestDTO, pet);

        // 3. Guardar entidad en la base de datos
        Medication savedMedication = medicationRepository.save(medication);

        // 4. Devolver DTO de respuesta
        return medicationMapper.toResponseDTO(savedMedication);
    }

    // -----------------------------------------------------
    // 2. OBTENER MEDICAMENTOS POR MASCOTA
    // -----------------------------------------------------

    @Transactional(readOnly = true)
    public List<MedicationResponseDTO> getMedicationsByPetId(Long petId) {
        // Validar que la mascota exista
        if (!petRepository.existsById(petId)) {
            throw new ResourceNotFoundException("Mascota no encontrada");
        }

        List<Medication> medications = medicationRepository.findByPetId(petId);
        return medications.stream().map(medicationMapper::toResponseDTO).collect(Collectors.toList());
    }

    // -----------------------------------------------------
    // 3. OBTENER MEDICAMENTO POR ID
    // -----------------------------------------------------
    @Transactional(readOnly = true)
    public MedicationResponseDTO getMedicationsById(Long id) {
        Medication medication = medicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado"));
        return medicationMapper.toResponseDTO(medication);
    }

    // -----------------------------------------------------
    // 4. ACTUALIZAR MEDICAMENTO
    // -----------------------------------------------------
    @Transactional
    public MedicationResponseDTO updateMedication(Long id, MedicationRequestDTO requestDTO) {
        Medication existingMedication = medicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medicacion no encontrada"));

        // Actualizar campos
        existingMedication.setMedicationName(requestDTO.getMedicationName());
        existingMedication.setDosage(requestDTO.getDosage());
        existingMedication.setFrequency(requestDTO.getFrequency());
        existingMedication.setStartDate(requestDTO.getStartDate());
        existingMedication.setEndDate(requestDTO.getEndDate());
        existingMedication.setReason(requestDTO.getReason());
        existingMedication.setAdministeringInstructions(requestDTO.getAdministeringInstructions());
        
        if (requestDTO.getIsActive() != null) {
            existingMedication.setActive(requestDTO.getIsActive());
        }

        Medication updatedMedication = medicationRepository.save(existingMedication);
        return medicationMapper.toResponseDTO(updatedMedication);
    }

    // -----------------------------------------------------
    // 5. ELIMINAR MEDICAMENTO (Lógico)
    // -----------------------------------------------------
    @Transactional
    public void deleteMedication(Long id) {
        Medication medication = medicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medicacion no encontrada"));
        
        medication.setActive(false);
        medicationRepository.save(medication);
    }
}
