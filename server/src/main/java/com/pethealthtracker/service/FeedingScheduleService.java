package com.pethealthtracker.service;

import com.pethealthtracker.dto.FeedingScheduleDto;

import java.util.List;

public interface FeedingScheduleService {
    List<FeedingScheduleDto> getFeedingSchedulesByPetId(Long petId);
    FeedingScheduleDto getFeedingScheduleById(Long id);
    FeedingScheduleDto createFeedingSchedule(FeedingScheduleDto feedingScheduleDto);
    FeedingScheduleDto updateFeedingSchedule(Long id, FeedingScheduleDto feedingScheduleDto);
    void deleteFeedingSchedule(Long id);
    List<FeedingScheduleDto> getActiveFeedingSchedulesByPetId(Long petId);
}
