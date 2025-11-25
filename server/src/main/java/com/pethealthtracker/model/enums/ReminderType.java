package com.pethealthtracker.model.enums;

public enum ReminderType {
    // Relacionados con la salud 
    VACCINATION, // Vacunacion
    DEWORMING, // Desparasitacion
    MEDICATION, // Medicacion
    APPOINTMENT, // Cita o consulta

    // Relacionados con el cuidado general
    GROOMING, // Aseo o peluqueria (baño, corte de pelo, etc.)
    FEEDING, // Recordatorio de alimentacion especial o dieta
    EXERCISE, // Horario de ejercicio o paseo

    // General
    OTHER
}
