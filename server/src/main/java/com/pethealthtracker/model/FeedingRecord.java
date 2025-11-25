package com.pethealthtracker.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feeding_records")
@Data
public class FeedingRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feeding_schedule_id")
    private FeedingSchedule feedingSchedule;

    @Column(name = "feeding_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime feedingTime = LocalDateTime.now();

    @Column(name = "food_type", length = 200)
    private String foodType;

    @Column(name = "portion_consumed", precision = 6)
    private Double portionConsumed;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "was_eaten", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean wasEaten = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
