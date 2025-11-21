package com.pethealthtracker.service.impl;

import com.pethealthtracker.dto.feeding.FeedingScheduleDto;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.mapper.FeedingScheduleMapper;
import com.pethealthtracker.model.FeedingSchedule;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.repository.FeedingScheduleRepository;
import com.pethealthtracker.repository.PetRepository;
import com.pethealthtracker.service.FeedingScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class FeedingScheduleServiceImpl implements FeedingScheduleService {

    private final FeedingScheduleRepository feedingScheduleRepository;
    private final PetRepository petRepository;

    @Autowired
    public FeedingScheduleServiceImpl(FeedingScheduleRepository feedingScheduleRepository,
                                    PetRepository petRepository) {
        this.feedingScheduleRepository = feedingScheduleRepository;
        this.petRepository = petRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedingScheduleDto> getFeedingSchedulesByPetId(Long petId) {
        return feedingScheduleRepository.findByPetId(petId).stream()
                .map(FeedingScheduleMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedingScheduleDto> getActiveFeedingSchedulesByPetId(Long petId) {
        return feedingScheduleRepository.findByPetIdAndActiveTrue(petId).stream()
                .map(FeedingScheduleMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FeedingScheduleDto getFeedingScheduleById(Long id) {
        return feedingScheduleRepository.findById(id)
                .map(FeedingScheduleMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingSchedule", "id", id));
    }

    @Override
    @Transactional
    public FeedingScheduleDto createFeedingSchedule(FeedingScheduleDto feedingScheduleDto) {
        Pet pet = petRepository.findById(feedingScheduleDto.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet", "id", feedingScheduleDto.getPetId()));

        FeedingSchedule feedingSchedule = FeedingScheduleMapper.INSTANCE.toEntity(feedingScheduleDto);
        feedingSchedule.setPet(pet);
        feedingSchedule.setActive(true);

        FeedingSchedule savedSchedule = feedingScheduleRepository.save(feedingSchedule);
        return FeedingScheduleMapper.INSTANCE.toDto(savedSchedule);
    }

    @Override
    @Transactional
    public FeedingScheduleDto updateFeedingSchedule(Long id, FeedingScheduleDto feedingScheduleDto) {
        FeedingSchedule existingSchedule = feedingScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingSchedule", "id", id));

        FeedingScheduleMapper.INSTANCE.updateEntityFromDto(feedingScheduleDto, existingSchedule);
        FeedingSchedule updatedSchedule = feedingScheduleRepository.save(existingSchedule);
        return FeedingScheduleMapper.INSTANCE.toDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteFeedingSchedule(Long id) {
        FeedingSchedule feedingSchedule = feedingScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingSchedule", "id", id));
        
        feedingSchedule.setActive(false);
        feedingScheduleRepository.save(feedingSchedule);
    }
}
