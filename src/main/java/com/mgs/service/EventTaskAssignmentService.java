package com.mgs.service;

import com.mgs.domain.EventTaskAssignment;
import com.mgs.repository.EventTaskAssignmentRepository;
import com.mgs.service.dto.EventTaskAssignmentDTO;
import com.mgs.service.mapper.EventTaskAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.EventTaskAssignment}.
 */
@Service
@Transactional
public class EventTaskAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(EventTaskAssignmentService.class);

    private final EventTaskAssignmentRepository eventTaskAssignmentRepository;

    private final EventTaskAssignmentMapper eventTaskAssignmentMapper;

    public EventTaskAssignmentService(
        EventTaskAssignmentRepository eventTaskAssignmentRepository,
        EventTaskAssignmentMapper eventTaskAssignmentMapper
    ) {
        this.eventTaskAssignmentRepository = eventTaskAssignmentRepository;
        this.eventTaskAssignmentMapper = eventTaskAssignmentMapper;
    }

    /**
     * Save a eventTaskAssignment.
     *
     * @param eventTaskAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public EventTaskAssignmentDTO save(EventTaskAssignmentDTO eventTaskAssignmentDTO) {
        LOG.debug("Request to save EventTaskAssignment : {}", eventTaskAssignmentDTO);
        EventTaskAssignment eventTaskAssignment = eventTaskAssignmentMapper.toEntity(eventTaskAssignmentDTO);
        eventTaskAssignment = eventTaskAssignmentRepository.save(eventTaskAssignment);
        return eventTaskAssignmentMapper.toDto(eventTaskAssignment);
    }

    /**
     * Update a eventTaskAssignment.
     *
     * @param eventTaskAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public EventTaskAssignmentDTO update(EventTaskAssignmentDTO eventTaskAssignmentDTO) {
        LOG.debug("Request to update EventTaskAssignment : {}", eventTaskAssignmentDTO);
        EventTaskAssignment eventTaskAssignment = eventTaskAssignmentMapper.toEntity(eventTaskAssignmentDTO);
        eventTaskAssignment = eventTaskAssignmentRepository.save(eventTaskAssignment);
        return eventTaskAssignmentMapper.toDto(eventTaskAssignment);
    }

    /**
     * Partially update a eventTaskAssignment.
     *
     * @param eventTaskAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventTaskAssignmentDTO> partialUpdate(EventTaskAssignmentDTO eventTaskAssignmentDTO) {
        LOG.debug("Request to partially update EventTaskAssignment : {}", eventTaskAssignmentDTO);

        return eventTaskAssignmentRepository
            .findById(eventTaskAssignmentDTO.getId())
            .map(existingEventTaskAssignment -> {
                eventTaskAssignmentMapper.partialUpdate(existingEventTaskAssignment, eventTaskAssignmentDTO);

                return existingEventTaskAssignment;
            })
            .map(eventTaskAssignmentRepository::save)
            .map(eventTaskAssignmentMapper::toDto);
    }

    /**
     * Get one eventTaskAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventTaskAssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get EventTaskAssignment : {}", id);
        return eventTaskAssignmentRepository.findById(id).map(eventTaskAssignmentMapper::toDto);
    }

    /**
     * Delete the eventTaskAssignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EventTaskAssignment : {}", id);
        eventTaskAssignmentRepository.deleteById(id);
    }
}
