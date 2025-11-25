package com.pethealthtracker.mapper;

import com.pethealthtracker.dto.feeding.FeedingRecordDto;
import com.pethealthtracker.model.FeedingRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FeedingRecordMapper {
    FeedingRecordMapper INSTANCE = Mappers.getMapper(FeedingRecordMapper.class);

    @Mapping(target = "petId", source = "pet.id")
    @Mapping(target = "feedingScheduleId", source = "feedingSchedule.id")
    FeedingRecordDto toDto(FeedingRecord feedingRecord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "feedingSchedule", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    FeedingRecord toEntity(FeedingRecordDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "feedingSchedule", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(FeedingRecordDto dto, @MappingTarget FeedingRecord entity);
}
