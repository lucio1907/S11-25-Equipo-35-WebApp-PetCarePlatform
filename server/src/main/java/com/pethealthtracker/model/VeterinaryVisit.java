package com.pethealthtracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "veterinary_visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SQLDelete(sql = "UPDATE veterinary_visits SET is_active = false WHERE id=?")
@Where(clause = "is_active=true")
public class VeterinaryVisit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "visit_date_time", nullable = false)
    private LocalDateTime visitDateTime;

    @Column(name = "veterinarian_name", nullable = false, length = 100)
    private String veterinarianName;

    @Column(nullable = false, length = 200)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @Column(name = "prescribed_medications", columnDefinition = "TEXT")
    private String prescribedMedications;

    @Column(name = "next_visit_date")
    private LocalDateTime nextVisitDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
