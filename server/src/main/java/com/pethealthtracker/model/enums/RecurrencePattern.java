package com.pethealthtracker.model.enums;

public enum RecurrencePattern {
    DAILY,          // Diario
    WEEKLY,         // Semanal (Mismo día de la semana)
    MONTHLY,        // Mensual (Mismo día del mes)
    YEARLY,         // Anual (Mismo día y mes)
    
    // Para casos especiales donde el patrón se define con la columna JSON
    CUSTOM          // Personalizado
}