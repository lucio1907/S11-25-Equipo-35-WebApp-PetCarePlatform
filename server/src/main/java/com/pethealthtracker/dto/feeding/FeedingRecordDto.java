package com.pethealthtracker.dto.feeding;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedingRecordDto {
    private Long id;
    private Long petId;
    private Long feedingScheduleId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime feedingTime;
    
    private String foodType;
    private Double portionConsumed;
    private String notes;
    private Boolean wasEaten;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
