package com.crm.service.impl;

import com.crm.domain.TaskAudit;
import com.crm.repository.TaskAuditRepository;
import com.crm.service.TaskAuditService;
import com.crm.service.dto.TaskAuditDTO;
import com.crm.service.mapper.TaskAuditMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.TaskAudit}.
 */
@Service
@Transactional
public class TaskAuditServiceImpl implements TaskAuditService {

    private final Logger log = LoggerFactory.getLogger(TaskAuditServiceImpl.class);

    private final TaskAuditRepository taskAuditRepository;

    private final TaskAuditMapper taskAuditMapper;

    public TaskAuditServiceImpl(TaskAuditRepository taskAuditRepository, TaskAuditMapper taskAuditMapper) {
        this.taskAuditRepository = taskAuditRepository;
        this.taskAuditMapper = taskAuditMapper;
    }

    @Override
    public TaskAuditDTO save(TaskAuditDTO taskAuditDTO) {
        log.debug("Request to save TaskAudit : {}", taskAuditDTO);
        TaskAudit taskAudit = taskAuditMapper.toEntity(taskAuditDTO);
        taskAudit = taskAuditRepository.save(taskAudit);
        return taskAuditMapper.toDto(taskAudit);
    }

    @Override
    public TaskAuditDTO update(TaskAuditDTO taskAuditDTO) {
        log.debug("Request to update TaskAudit : {}", taskAuditDTO);
        TaskAudit taskAudit = taskAuditMapper.toEntity(taskAuditDTO);
        taskAudit = taskAuditRepository.save(taskAudit);
        return taskAuditMapper.toDto(taskAudit);
    }

    @Override
    public Optional<TaskAuditDTO> partialUpdate(TaskAuditDTO taskAuditDTO) {
        log.debug("Request to partially update TaskAudit : {}", taskAuditDTO);

        return taskAuditRepository
            .findById(taskAuditDTO.getId())
            .map(existingTaskAudit -> {
                taskAuditMapper.partialUpdate(existingTaskAudit, taskAuditDTO);

                return existingTaskAudit;
            })
            .map(taskAuditRepository::save)
            .map(taskAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskAuditDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaskAudits");
        return taskAuditRepository.findAll(pageable).map(taskAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskAuditDTO> findOne(Long id) {
        log.debug("Request to get TaskAudit : {}", id);
        return taskAuditRepository.findById(id).map(taskAuditMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskAudit : {}", id);
        taskAuditRepository.deleteById(id);
    }
}
