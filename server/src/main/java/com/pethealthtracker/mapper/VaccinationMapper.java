package com.pethealthtracker.mapper;

import com.pethealthtracker.dto.vaccinations.VaccinationRequestDTO;
import com.pethealthtracker.dto.vaccinations.VaccinationResponseDTO;
import com.pethealthtracker.model.Vaccination;
import com.pethealthtracker.repository.PetRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class VaccinationMapper {

    @Autowired
    protected PetRepository petRepository;
    
    public Vaccination toEntity(VaccinationRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Vaccination vaccination = new Vaccination();
        vaccination.setVaccineName(dto.getVaccineName());
        vaccination.setManufacturer(dto.getManufacturer());
        vaccination.setBatchNumber(dto.getBatchNumber());
        vaccination.setAdministrationDate(dto.getAdministrationDate());
        vaccination.setExpirationDate(dto.getExpirationDate());
        vaccination.setNextDoseDate(dto.getNextDoseDate());
        vaccination.setAdministeredBy(dto.getAdministeredBy());
        vaccination.setNotes(dto.getNotes());
        vaccination.setIsActive(true);
        
        return vaccination;
    }
    
    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "pet.name", target = "petName")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd")
    public abstract VaccinationResponseDTO toDto(Vaccination entity);
    
    public void updateVaccinationFromDto(VaccinationRequestDTO dto, @MappingTarget Vaccination entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        if (dto.getVaccineName() != null) {
            entity.setVaccineName(dto.getVaccineName());
        }
        if (dto.getManufacturer() != null) {
            entity.setManufacturer(dto.getManufacturer());
        }
        if (dto.getBatchNumber() != null) {
            entity.setBatchNumber(dto.getBatchNumber());
        }
        if (dto.getAdministrationDate() != null) {
            entity.setAdministrationDate(dto.getAdministrationDate());
        }
        if (dto.getExpirationDate() != null) {
            entity.setExpirationDate(dto.getExpirationDate());
        }
        if (dto.getNextDoseDate() != null) {
            entity.setNextDoseDate(dto.getNextDoseDate());
        }
        if (dto.getAdministeredBy() != null) {
            entity.setAdministeredBy(dto.getAdministeredBy());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
    }
}
