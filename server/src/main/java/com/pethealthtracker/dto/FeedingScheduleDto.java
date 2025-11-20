package com.pethealthtracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pethealthtracker.model.FeedingFrequency;
import com.pethealthtracker.model.PortionUnit;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedingScheduleDto {
    private Long id;
    private Long petId;
    private String foodType;
    private String foodBrand;
    private Double portionSize;
    private PortionUnit portionUnit;
    private FeedingFrequency feedingFrequency;
    private String customSchedule; // JSON string
    private String specialInstructions;
    private Boolean isActive;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
