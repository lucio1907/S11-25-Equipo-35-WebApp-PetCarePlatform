package com.pethealthtracker.model;

import com.pethealthtracker.model.enums.Gender;
import com.pethealthtracker.model.enums.PetSpecies;
import com.pethealthtracker.model.enums.WeightUnit;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, of = "id") // Comparamos solo por ID para evitar ciclos
@ToString(exclude = "user") // Evitamos bucles infinitos en los logs
@EntityListeners(AuditingEntityListener.class) // Activamos la auditoría automática
public class Pet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetSpecies species;

    @Column(name = "breed", length = 100)
    private String breed;

    @Column(length = 100)
    private String color;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "age_years")
    private Integer ageYears;

    @Column(name = "age_months")
    private Integer ageMonths;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_unit")
    private WeightUnit weightUnit;

    @Column(name = "microchip_number", length = 50, unique = true)
    private String microchipNumber;

    @Column(name = "health_notes", columnDefinition = "TEXT")
    private String healthNotes;

    @Column(name = "profile_picture_url", length = 255)
    private String profilePictureUrl;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}