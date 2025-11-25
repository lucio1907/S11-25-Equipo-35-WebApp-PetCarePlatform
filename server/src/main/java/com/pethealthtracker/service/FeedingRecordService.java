package com.pethealthtracker.service;

import com.pethealthtracker.dto.feeding.FeedingRecordDto;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.mapper.FeedingRecordMapper;
import com.pethealthtracker.model.FeedingRecord;
import com.pethealthtracker.model.FeedingSchedule;
import com.pethealthtracker.repository.FeedingRecordRepository;
import com.pethealthtracker.repository.FeedingScheduleRepository;
import com.pethealthtracker.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Servicio para gestionar los registros de alimentación de mascotas.
 * Proporciona operaciones CRUD y consultas específicas para los registros de alimentación.
 */
@Slf4j

@Service
@RequiredArgsConstructor
public class FeedingRecordService {

    private static final String FEEDING_RECORD_NOT_FOUND = "Registro de alimentación no encontrado con ID: %d";
    private static final String FEEDING_SCHEDULE_NOT_FOUND = "Horario de alimentación no encontrado con ID: %d";
    private static final String INVALID_FEEDING_RECORD = "Los datos del registro de alimentación no pueden ser nulos";
    private static final String PET_ID_REQUIRED = "El ID de la mascota es requerido";

    private final FeedingRecordRepository feedingRecordRepository;
    private final FeedingScheduleRepository feedingScheduleRepository;
    private final FeedingRecordMapper feedingRecordMapper;

    /**
     * Obtiene todos los registros de alimentación de una mascota específica.
     *
     * @param petId ID de la mascota
     * @return Lista de registros de alimentación
     */
    @Transactional(readOnly = true)
    public List<FeedingRecord> getFeedingRecordsByPetId(Long petId) {
            log.debug("Obteniendo registros de alimentación para la mascota con ID: {}", petId);
            return feedingRecordRepository.findByPetId(petId);
        }

    /**
     * Obtiene los registros de alimentación de una mascota en un rango de fechas.
     *
     * @param petId     ID de la mascota
     * @param startDate Fecha de inicio del rango (inclusive)
     * @param endDate   Fecha de fin del rango (inclusive)
     * @return Lista de registros de alimentación en el rango especificado
     */
    @Transactional(readOnly = true)
    public List<FeedingRecord> getFeedingRecordsByPetIdAndDateRange(
            Long petId, LocalDateTime startDate, LocalDateTime endDate) {
            log.debug("Obteniendo registros de alimentación para la mascota {} entre {} y {}",
                    petId, startDate, endDate);
            return feedingRecordRepository.findByPetIdAndFeedingTimeBetween(petId, startDate, endDate);
        }

    /**
     * Obtiene un registro de alimentación por su ID.
     *
     * @param id ID del registro de alimentación
     * @return El registro de alimentación encontrado
     * @throws ResourceNotFoundException si no se encuentra el registro
     */
    @Transactional(readOnly = true)
    public FeedingRecord getFeedingRecordById(Long id) {
            log.debug("Buscando registro de alimentación con ID: {}", id);
            return feedingRecordRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(FEEDING_RECORD_NOT_FOUND, id)));
        }

    /**
     * Crea un nuevo registro de alimentación.
     *
     * @param feedingRecordDto DTO con los datos del registro de alimentación
     * @return El registro de alimentación creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    @Transactional
    public FeedingRecord createFeedingRecord(FeedingRecordDto feedingRecordDto) {
            log.debug("Creando nuevo registro de alimentación: {}", feedingRecordDto);

            if (feedingRecordDto == null) {
                throw new IllegalArgumentException(INVALID_FEEDING_RECORD);
            }

            if (feedingRecordDto.getPetId() == null) {
                throw new IllegalArgumentException(PET_ID_REQUIRED);
            }

            FeedingRecord feedingRecord = feedingRecordMapper.toEntity(feedingRecordDto);

            // Establecer valores por defecto
            feedingRecord.setFeedingTime(feedingRecord.getFeedingTime() != null ?
                    feedingRecord.getFeedingTime() : LocalDateTime.now());

            if (feedingRecord.getWasEaten() == null) {
                feedingRecord.setWasEaten(true);
            }

            FeedingRecord savedRecord = feedingRecordRepository.save(feedingRecord);
            log.debug("Registro de alimentación creado exitosamente con ID: {}", savedRecord.getId());
            return savedRecord;
        }

    /**
     * Actualiza un registro de alimentación existente.
     *
     * @param id               ID del registro a actualizar
     * @param feedingRecordDto DTO con los datos actualizados
     * @return El registro de alimentación actualizado
     * @throws ResourceNotFoundException si no se encuentra el registro
     * @throws IllegalArgumentException  si los datos son inválidos
     */
    @Transactional
    public FeedingRecord updateFeedingRecord(Long id, FeedingRecordDto feedingRecordDto) {
            log.debug("Actualizando registro de alimentación con ID: {}", id);

            if (feedingRecordDto == null) {
                throw new IllegalArgumentException(INVALID_FEEDING_RECORD);
            }

            FeedingRecord existingRecord = feedingRecordRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(FEEDING_RECORD_NOT_FOUND, id)));

            updateFeedingRecordFields(existingRecord, feedingRecordDto);

            FeedingRecord updatedRecord = feedingRecordRepository.save(existingRecord);
            log.debug("Registro de alimentación con ID: {} actualizado exitosamente", id);
            return updatedRecord;
        }

    /**
     * Actualiza los campos de un registro de alimentación a partir de un DTO.
     *
     * @param record Registro existente a actualizar
     * @param dto    DTO con los nuevos valores
     */
    private void updateFeedingRecordFields(FeedingRecord record, FeedingRecordDto dto) {
            if (dto.getFoodType() != null) {
                record.setFoodType(dto.getFoodType());
            }

            if (dto.getPortionConsumed() != null) {
                record.setPortionConsumed(dto.getPortionConsumed());
            }

            if (dto.getNotes() != null) {
                record.setNotes(dto.getNotes());
            }

            if (dto.getWasEaten() != null) {
                record.setWasEaten(dto.getWasEaten());
            }

            updateFeedingSchedule(record, dto.getFeedingScheduleId());
        }

    /**
     * Actualiza el horario de alimentación de un registro.
     *
     * @param record     Registro a actualizar
     * @param scheduleId ID del nuevo horario de alimentación (puede ser null)
     */
    private void updateFeedingSchedule(FeedingRecord record, Long scheduleId) {
            if (scheduleId != null) {
                FeedingSchedule schedule = feedingScheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format(FEEDING_SCHEDULE_NOT_FOUND, scheduleId)));
                record.setFeedingSchedule(schedule);
            } else if (scheduleId == null && record.getFeedingSchedule() != null) {
                record.setFeedingSchedule(null);
            }
        }

    /**
     * Elimina un registro de alimentación por su ID.
     *
     * @param id ID del registro a eliminar
     * @throws ResourceNotFoundException si no se encuentra el registro
     */
    @Transactional
    public void deleteFeedingRecord(Long id) {
            log.debug("Eliminando registro de alimentación con ID: {}", id);

            if (!feedingRecordRepository.existsById(id)) {
                throw new ResourceNotFoundException(String.format(FEEDING_RECORD_NOT_FOUND, id));
            }

            feedingRecordRepository.deleteById(id);
            log.debug("Registro de alimentación con ID: {} eliminado exitosamente", id);
        }
    }