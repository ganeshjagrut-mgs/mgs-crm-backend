package com.mgs.service;

import com.mgs.domain.TaskType;
import com.mgs.repository.TaskTypeRepository;
import com.mgs.service.dto.TaskTypeDTO;
import com.mgs.service.mapper.TaskTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.TaskType}.
 */
@Service
@Transactional
public class TaskTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskTypeService.class);

    private final TaskTypeRepository taskTypeRepository;

    private final TaskTypeMapper taskTypeMapper;

    public TaskTypeService(TaskTypeRepository taskTypeRepository, TaskTypeMapper taskTypeMapper) {
        this.taskTypeRepository = taskTypeRepository;
        this.taskTypeMapper = taskTypeMapper;
    }

    /**
     * Save a taskType.
     *
     * @param taskTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TaskTypeDTO save(TaskTypeDTO taskTypeDTO) {
        LOG.debug("Request to save TaskType : {}", taskTypeDTO);
        TaskType taskType = taskTypeMapper.toEntity(taskTypeDTO);
        taskType = taskTypeRepository.save(taskType);
        return taskTypeMapper.toDto(taskType);
    }

    /**
     * Update a taskType.
     *
     * @param taskTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TaskTypeDTO update(TaskTypeDTO taskTypeDTO) {
        LOG.debug("Request to update TaskType : {}", taskTypeDTO);
        TaskType taskType = taskTypeMapper.toEntity(taskTypeDTO);
        taskType = taskTypeRepository.save(taskType);
        return taskTypeMapper.toDto(taskType);
    }

    /**
     * Partially update a taskType.
     *
     * @param taskTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TaskTypeDTO> partialUpdate(TaskTypeDTO taskTypeDTO) {
        LOG.debug("Request to partially update TaskType : {}", taskTypeDTO);

        return taskTypeRepository
            .findById(taskTypeDTO.getId())
            .map(existingTaskType -> {
                taskTypeMapper.partialUpdate(existingTaskType, taskTypeDTO);

                return existingTaskType;
            })
            .map(taskTypeRepository::save)
            .map(taskTypeMapper::toDto);
    }

    /**
     * Get one taskType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TaskTypeDTO> findOne(Long id) {
        LOG.debug("Request to get TaskType : {}", id);
        return taskTypeRepository.findById(id).map(taskTypeMapper::toDto);
    }

    /**
     * Delete the taskType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TaskType : {}", id);
        taskTypeRepository.deleteById(id);
    }
}
