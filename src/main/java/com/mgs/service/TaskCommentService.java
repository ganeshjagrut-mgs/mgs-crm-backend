package com.mgs.service;

import com.mgs.domain.TaskComment;
import com.mgs.repository.TaskCommentRepository;
import com.mgs.service.dto.TaskCommentDTO;
import com.mgs.service.mapper.TaskCommentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.TaskComment}.
 */
@Service
@Transactional
public class TaskCommentService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskCommentService.class);

    private final TaskCommentRepository taskCommentRepository;

    private final TaskCommentMapper taskCommentMapper;

    public TaskCommentService(TaskCommentRepository taskCommentRepository, TaskCommentMapper taskCommentMapper) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskCommentMapper = taskCommentMapper;
    }

    /**
     * Save a taskComment.
     *
     * @param taskCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public TaskCommentDTO save(TaskCommentDTO taskCommentDTO) {
        LOG.debug("Request to save TaskComment : {}", taskCommentDTO);
        TaskComment taskComment = taskCommentMapper.toEntity(taskCommentDTO);
        taskComment = taskCommentRepository.save(taskComment);
        return taskCommentMapper.toDto(taskComment);
    }

    /**
     * Update a taskComment.
     *
     * @param taskCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public TaskCommentDTO update(TaskCommentDTO taskCommentDTO) {
        LOG.debug("Request to update TaskComment : {}", taskCommentDTO);
        TaskComment taskComment = taskCommentMapper.toEntity(taskCommentDTO);
        taskComment = taskCommentRepository.save(taskComment);
        return taskCommentMapper.toDto(taskComment);
    }

    /**
     * Partially update a taskComment.
     *
     * @param taskCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TaskCommentDTO> partialUpdate(TaskCommentDTO taskCommentDTO) {
        LOG.debug("Request to partially update TaskComment : {}", taskCommentDTO);

        return taskCommentRepository
            .findById(taskCommentDTO.getId())
            .map(existingTaskComment -> {
                taskCommentMapper.partialUpdate(existingTaskComment, taskCommentDTO);

                return existingTaskComment;
            })
            .map(taskCommentRepository::save)
            .map(taskCommentMapper::toDto);
    }

    /**
     * Get one taskComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TaskCommentDTO> findOne(Long id) {
        LOG.debug("Request to get TaskComment : {}", id);
        return taskCommentRepository.findById(id).map(taskCommentMapper::toDto);
    }

    /**
     * Delete the taskComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TaskComment : {}", id);
        taskCommentRepository.deleteById(id);
    }
}
