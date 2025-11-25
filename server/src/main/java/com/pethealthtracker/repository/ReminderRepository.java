package com.pethealthtracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pethealthtracker.model.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    // Consultas personalizadas

    // 1. Encontrar recordatorios por ID de usuario
    List<Reminder> findByUserId(Long userId);

    // 2. Encontrar recordatorios por ID de mascota
    List<Reminder> findByPetId(Long petId);

    // 3. Encontrar recordatorios por ID de usuario y ID de mascota
    List<Reminder> findByUserIdAndPetId(Long userId, Long petId);

    // 4. Encontrar recordatorios incompletos
    List<Reminder> findByIsCompletedFalse();

    // 5. Encontrar recordatorios incompletos por ID de usuario
    List<Reminder> findByUserIdAndIsCompletedFalse(Long userId);

    // 6. Encontrar recordatorios vencidos
    List<Reminder> findByIsCompletedFalseAndDueDateLessThanEqual(LocalDate dueDate);

    // 7. Encontrar recordatorios recurrentes
    List<Reminder> findByIsRecurringTrue();
}
