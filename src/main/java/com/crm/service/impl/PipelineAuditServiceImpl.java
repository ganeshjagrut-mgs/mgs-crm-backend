package com.crm.service.impl;

import com.crm.domain.PipelineAudit;
import com.crm.repository.PipelineAuditRepository;
import com.crm.service.PipelineAuditService;
import com.crm.service.dto.PipelineAuditDTO;
import com.crm.service.mapper.PipelineAuditMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.PipelineAudit}.
 */
@Service
@Transactional
public class PipelineAuditServiceImpl implements PipelineAuditService {

    private final Logger log = LoggerFactory.getLogger(PipelineAuditServiceImpl.class);

    private final PipelineAuditRepository pipelineAuditRepository;

    private final PipelineAuditMapper pipelineAuditMapper;

    public PipelineAuditServiceImpl(PipelineAuditRepository pipelineAuditRepository, PipelineAuditMapper pipelineAuditMapper) {
        this.pipelineAuditRepository = pipelineAuditRepository;
        this.pipelineAuditMapper = pipelineAuditMapper;
    }

    @Override
    public PipelineAuditDTO save(PipelineAuditDTO pipelineAuditDTO) {
        log.debug("Request to save PipelineAudit : {}", pipelineAuditDTO);
        PipelineAudit pipelineAudit = pipelineAuditMapper.toEntity(pipelineAuditDTO);
        pipelineAudit = pipelineAuditRepository.save(pipelineAudit);
        return pipelineAuditMapper.toDto(pipelineAudit);
    }

    @Override
    public PipelineAuditDTO update(PipelineAuditDTO pipelineAuditDTO) {
        log.debug("Request to update PipelineAudit : {}", pipelineAuditDTO);
        PipelineAudit pipelineAudit = pipelineAuditMapper.toEntity(pipelineAuditDTO);
        pipelineAudit = pipelineAuditRepository.save(pipelineAudit);
        return pipelineAuditMapper.toDto(pipelineAudit);
    }

    @Override
    public Optional<PipelineAuditDTO> partialUpdate(PipelineAuditDTO pipelineAuditDTO) {
        log.debug("Request to partially update PipelineAudit : {}", pipelineAuditDTO);

        return pipelineAuditRepository
            .findById(pipelineAuditDTO.getId())
            .map(existingPipelineAudit -> {
                pipelineAuditMapper.partialUpdate(existingPipelineAudit, pipelineAuditDTO);

                return existingPipelineAudit;
            })
            .map(pipelineAuditRepository::save)
            .map(pipelineAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PipelineAuditDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PipelineAudits");
        return pipelineAuditRepository.findAll(pageable).map(pipelineAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PipelineAuditDTO> findOne(Long id) {
        log.debug("Request to get PipelineAudit : {}", id);
        return pipelineAuditRepository.findById(id).map(pipelineAuditMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PipelineAudit : {}", id);
        pipelineAuditRepository.deleteById(id);
    }
}
