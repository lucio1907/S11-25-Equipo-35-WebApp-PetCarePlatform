package com.pethealthtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pethealthtracker.model.enums.FeedingFrequency;
import com.pethealthtracker.model.enums.PortionUnit;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feeding_schedule")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FeedingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "food_type", nullable = false)
    private String foodType;

    @Column(name = "food_brand")
    private String foodBrand;

    @Column(name = "portion_size", columnDefinition = "DECIMAL(10,2)")
    private Double portionSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "portion_unit", length = 10) 
    private PortionUnit portionUnit = PortionUnit.G;

    @Enumerated(EnumType.STRING)
    @Column(name = "feeding_frequency", nullable = false, length = 20)
    private FeedingFrequency feedingFrequency;

    @Column(name = "custom_schedule", columnDefinition = "jsonb") 
    private String customSchedule;

    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}