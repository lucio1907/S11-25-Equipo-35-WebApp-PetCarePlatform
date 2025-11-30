package com.pethealthtracker.service;

import com.pethealthtracker.dto.veterinary_visits.VeterinaryVisitRequestDTO;
import com.pethealthtracker.dto.veterinary_visits.VeterinaryVisitResponseDTO;

import java.util.List;

public interface VeterinaryVisitService {
    
    VeterinaryVisitResponseDTO createVeterinaryVisit(Long userId, Long petId, VeterinaryVisitRequestDTO requestDTO);
    
    VeterinaryVisitResponseDTO getVeterinaryVisitById(Long userId, Long petId, Long visitId);
    
    List<VeterinaryVisitResponseDTO> getVeterinaryVisitsByPetId(Long userId, Long petId);
    
    VeterinaryVisitResponseDTO updateVeterinaryVisit(
        Long userId, Long petId, Long visitId, VeterinaryVisitRequestDTO requestDTO);
    
    void deleteVeterinaryVisit(Long userId, Long petId, Long visitId);
}
