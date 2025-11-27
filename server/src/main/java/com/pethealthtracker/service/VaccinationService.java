package com.pethealthtracker.service;

import com.pethealthtracker.dto.vaccinations.VaccinationRequestDTO;
import com.pethealthtracker.dto.vaccinations.VaccinationResponseDTO;

import java.util.List;

public interface VaccinationService {
    
    VaccinationResponseDTO createVaccination(Long userId, Long petId, VaccinationRequestDTO requestDTO);
    
    VaccinationResponseDTO getVaccinationById(Long userId, Long petId, Long vaccinationId);
    
    List<VaccinationResponseDTO> getVaccinationsByPetId(Long userId, Long petId);
    
    VaccinationResponseDTO updateVaccination(Long userId, Long petId, Long vaccinationId, VaccinationRequestDTO requestDTO);
    
    void deleteVaccination(Long userId, Long petId, Long vaccinationId);
}
