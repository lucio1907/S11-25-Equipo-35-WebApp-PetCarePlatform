package com.pethealthtracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "vaccinations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Vaccination extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "vaccine_name", nullable = false, length = 100)
    private String vaccineName;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column(name = "administration_date", nullable = false)
    private LocalDate administrationDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "next_dose_date")
    private LocalDate nextDoseDate;

    @Column(name = "administered_by", length = 100)
    private String administeredBy;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

}
