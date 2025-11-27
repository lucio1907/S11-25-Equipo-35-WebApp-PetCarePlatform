package com.pethealthtracker.service;

import java.util.List;

import com.pethealthtracker.dto.medications.MedicationRequestDTO;
import com.pethealthtracker.dto.medications.MedicationResponseDTO;

public interface MedicationService {
    MedicationResponseDTO createMedication(MedicationRequestDTO medicationRequestDTO);

    List<MedicationResponseDTO> getMedicationsByPetId(Long petId);

    MedicationResponseDTO getMedicationsById(Long id);

    MedicationResponseDTO updateMedication(Long id, MedicationRequestDTO requestDTO); 

    void deleteMedication(Long id);
}
