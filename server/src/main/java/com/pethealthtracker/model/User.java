package com.pethealthtracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un usuario del sistema.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, of = "id") // Comparamos solo por ID
@EntityListeners(AuditingEntityListener.class) // Importante para que funcionen las fechas automáticas
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @NaturalId
    @NotBlank
    @Size(max = 100)
    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 20)
    private String phone;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Builder.Default
    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "reset_token_expires")
    private LocalDateTime resetTokenExpires;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pet> pets = new HashSet<>();

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