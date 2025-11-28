package com.mgs.service;

import com.mgs.domain.EventNotification;
import com.mgs.repository.EventNotificationRepository;
import com.mgs.service.dto.EventNotificationDTO;
import com.mgs.service.mapper.EventNotificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.EventNotification}.
 */
@Service
@Transactional
public class EventNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(EventNotificationService.class);

    private final EventNotificationRepository eventNotificationRepository;

    private final EventNotificationMapper eventNotificationMapper;

    public EventNotificationService(
        EventNotificationRepository eventNotificationRepository,
        EventNotificationMapper eventNotificationMapper
    ) {
        this.eventNotificationRepository = eventNotificationRepository;
        this.eventNotificationMapper = eventNotificationMapper;
    }

    /**
     * Save a eventNotification.
     *
     * @param eventNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public EventNotificationDTO save(EventNotificationDTO eventNotificationDTO) {
        LOG.debug("Request to save EventNotification : {}", eventNotificationDTO);
        EventNotification eventNotification = eventNotificationMapper.toEntity(eventNotificationDTO);
        eventNotification = eventNotificationRepository.save(eventNotification);
        return eventNotificationMapper.toDto(eventNotification);
    }

    /**
     * Update a eventNotification.
     *
     * @param eventNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public EventNotificationDTO update(EventNotificationDTO eventNotificationDTO) {
        LOG.debug("Request to update EventNotification : {}", eventNotificationDTO);
        EventNotification eventNotification = eventNotificationMapper.toEntity(eventNotificationDTO);
        eventNotification = eventNotificationRepository.save(eventNotification);
        return eventNotificationMapper.toDto(eventNotification);
    }

    /**
     * Partially update a eventNotification.
     *
     * @param eventNotificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventNotificationDTO> partialUpdate(EventNotificationDTO eventNotificationDTO) {
        LOG.debug("Request to partially update EventNotification : {}", eventNotificationDTO);

        return eventNotificationRepository
            .findById(eventNotificationDTO.getId())
            .map(existingEventNotification -> {
                eventNotificationMapper.partialUpdate(existingEventNotification, eventNotificationDTO);

                return existingEventNotification;
            })
            .map(eventNotificationRepository::save)
            .map(eventNotificationMapper::toDto);
    }

    /**
     * Get one eventNotification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventNotificationDTO> findOne(Long id) {
        LOG.debug("Request to get EventNotification : {}", id);
        return eventNotificationRepository.findById(id).map(eventNotificationMapper::toDto);
    }

    /**
     * Delete the eventNotification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EventNotification : {}", id);
        eventNotificationRepository.deleteById(id);
    }
}
