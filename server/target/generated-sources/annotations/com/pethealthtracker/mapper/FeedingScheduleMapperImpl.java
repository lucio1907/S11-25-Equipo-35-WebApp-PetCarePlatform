package com.pethealthtracker.mapper;

import com.pethealthtracker.dto.FeedingScheduleDto;
import com.pethealthtracker.model.FeedingSchedule;
import com.pethealthtracker.model.Pet;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-20T09:01:38-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class FeedingScheduleMapperImpl extends FeedingScheduleMapper {

    @Override
    public FeedingScheduleDto toDto(FeedingSchedule feedingSchedule) {
        if ( feedingSchedule == null ) {
            return null;
        }

        FeedingScheduleDto feedingScheduleDto = new FeedingScheduleDto();

        feedingScheduleDto.setPetId( feedingSchedulePetId( feedingSchedule ) );
        feedingScheduleDto.setIsActive( feedingSchedule.isActive() );
        feedingScheduleDto.setId( feedingSchedule.getId() );
        feedingScheduleDto.setFoodType( feedingSchedule.getFoodType() );
        feedingScheduleDto.setFoodBrand( feedingSchedule.getFoodBrand() );
        feedingScheduleDto.setPortionSize( feedingSchedule.getPortionSize() );
        feedingScheduleDto.setPortionUnit( feedingSchedule.getPortionUnit() );
        feedingScheduleDto.setFeedingFrequency( feedingSchedule.getFeedingFrequency() );
        feedingScheduleDto.setCustomSchedule( feedingSchedule.getCustomSchedule() );
        feedingScheduleDto.setSpecialInstructions( feedingSchedule.getSpecialInstructions() );
        feedingScheduleDto.setCreatedAt( feedingSchedule.getCreatedAt() );
        feedingScheduleDto.setUpdatedAt( feedingSchedule.getUpdatedAt() );

        return feedingScheduleDto;
    }

    @Override
    public FeedingSchedule toEntity(FeedingScheduleDto feedingScheduleDto) {
        if ( feedingScheduleDto == null ) {
            return null;
        }

        FeedingSchedule feedingSchedule = new FeedingSchedule();

        if ( feedingScheduleDto.getIsActive() != null ) {
            feedingSchedule.setActive( feedingScheduleDto.getIsActive() );
        }
        feedingSchedule.setFoodType( feedingScheduleDto.getFoodType() );
        feedingSchedule.setFoodBrand( feedingScheduleDto.getFoodBrand() );
        feedingSchedule.setPortionSize( feedingScheduleDto.getPortionSize() );
        feedingSchedule.setPortionUnit( feedingScheduleDto.getPortionUnit() );
        feedingSchedule.setFeedingFrequency( feedingScheduleDto.getFeedingFrequency() );
        feedingSchedule.setCustomSchedule( feedingScheduleDto.getCustomSchedule() );
        feedingSchedule.setSpecialInstructions( feedingScheduleDto.getSpecialInstructions() );

        return feedingSchedule;
    }

    @Override
    public void updateEntityFromDto(FeedingScheduleDto dto, FeedingSchedule entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getIsActive() != null ) {
            entity.setActive( dto.getIsActive() );
        }
        if ( dto.getFoodType() != null ) {
            entity.setFoodType( dto.getFoodType() );
        }
        if ( dto.getFoodBrand() != null ) {
            entity.setFoodBrand( dto.getFoodBrand() );
        }
        if ( dto.getPortionSize() != null ) {
            entity.setPortionSize( dto.getPortionSize() );
        }
        if ( dto.getPortionUnit() != null ) {
            entity.setPortionUnit( dto.getPortionUnit() );
        }
        if ( dto.getFeedingFrequency() != null ) {
            entity.setFeedingFrequency( dto.getFeedingFrequency() );
        }
        if ( dto.getCustomSchedule() != null ) {
            entity.setCustomSchedule( dto.getCustomSchedule() );
        }
        if ( dto.getSpecialInstructions() != null ) {
            entity.setSpecialInstructions( dto.getSpecialInstructions() );
        }
    }

    private Long feedingSchedulePetId(FeedingSchedule feedingSchedule) {
        if ( feedingSchedule == null ) {
            return null;
        }
        Pet pet = feedingSchedule.getPet();
        if ( pet == null ) {
            return null;
        }
        Long id = pet.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
