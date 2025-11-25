package com.pethealthtracker.repository;

import com.pethealthtracker.model.FeedingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedingRecordRepository extends JpaRepository<FeedingRecord, Long> {
    List<FeedingRecord> findByPetId(Long petId);
    List<FeedingRecord> findByPetIdAndFeedingTimeBetween(Long petId, LocalDateTime start, LocalDateTime end);
    List<FeedingRecord> findByFeedingScheduleId(Long scheduleId);
    List<FeedingRecord> findByWasEaten(Boolean wasEaten);
}
