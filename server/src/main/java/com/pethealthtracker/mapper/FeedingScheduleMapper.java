package com.pethealthtracker.mapper;

import com.pethealthtracker.dto.feeding.FeedingScheduleDto;
import com.pethealthtracker.model.FeedingSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class FeedingScheduleMapper {
    
    public static FeedingScheduleMapper INSTANCE;
    
    public FeedingScheduleMapper() {
        INSTANCE = this;
    }
    
    @Mapping(target = "petId", source = "pet.id")
    @Mapping(target = "isActive", source = "active")
    public abstract FeedingScheduleDto toDto(FeedingSchedule feedingSchedule);
    
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", source = "isActive")
    public abstract FeedingSchedule toEntity(FeedingScheduleDto feedingScheduleDto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", source = "isActive")
    public abstract void updateEntityFromDto(FeedingScheduleDto dto, @MappingTarget FeedingSchedule entity);
}
