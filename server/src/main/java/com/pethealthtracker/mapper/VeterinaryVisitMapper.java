package com.pethealthtracker.mapper;

import com.pethealthtracker.dto.veterinary_visits.VeterinaryVisitRequestDTO;
import com.pethealthtracker.dto.veterinary_visits.VeterinaryVisitResponseDTO;
import com.pethealthtracker.model.VeterinaryVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VeterinaryVisitMapper {

    VeterinaryVisitMapper INSTANCE = Mappers.getMapper(VeterinaryVisitMapper.class);

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    VeterinaryVisit toEntity(VeterinaryVisitRequestDTO dto);

    @Mapping(target = "petId", source = "pet.id")
    @Mapping(target = "petName", source = "pet.name")
    VeterinaryVisitResponseDTO toDto(VeterinaryVisit entity);

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateFromDto(VeterinaryVisitRequestDTO dto, @MappingTarget VeterinaryVisit entity);
}
