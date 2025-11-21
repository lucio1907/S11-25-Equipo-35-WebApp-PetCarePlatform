package com.pethealthtracker.service;

import java.util.List;

import com.pethealthtracker.dto.feeding.FeedingScheduleDto;

public interface FeedingScheduleService {
    List<FeedingScheduleDto> getFeedingSchedulesByPetId(Long petId);
    FeedingScheduleDto getFeedingScheduleById(Long id);
    FeedingScheduleDto createFeedingSchedule(FeedingScheduleDto feedingScheduleDto);
    FeedingScheduleDto updateFeedingSchedule(Long id, FeedingScheduleDto feedingScheduleDto);
    void deleteFeedingSchedule(Long id);
    List<FeedingScheduleDto> getActiveFeedingSchedulesByPetId(Long petId);
}
