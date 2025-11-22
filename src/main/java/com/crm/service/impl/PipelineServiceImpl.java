package com.crm.service.impl;

import com.crm.domain.Pipeline;
import com.crm.repository.PipelineRepository;
import com.crm.service.PipelineService;
import com.crm.service.dto.PipelineDTO;
import com.crm.service.mapper.PipelineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.Pipeline}.
 */
@Service
@Transactional
public class PipelineServiceImpl implements PipelineService {

    private final Logger log = LoggerFactory.getLogger(PipelineServiceImpl.class);

    private final PipelineRepository pipelineRepository;

    private final PipelineMapper pipelineMapper;

    public PipelineServiceImpl(PipelineRepository pipelineRepository, PipelineMapper pipelineMapper) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineMapper = pipelineMapper;
    }

    @Override
    public PipelineDTO save(PipelineDTO pipelineDTO) {
        log.debug("Request to save Pipeline : {}", pipelineDTO);
        Pipeline pipeline = pipelineMapper.toEntity(pipelineDTO);
        pipeline = pipelineRepository.save(pipeline);
        return pipelineMapper.toDto(pipeline);
    }

    @Override
    public PipelineDTO update(PipelineDTO pipelineDTO) {
        log.debug("Request to update Pipeline : {}", pipelineDTO);
        Pipeline pipeline = pipelineMapper.toEntity(pipelineDTO);
        pipeline = pipelineRepository.save(pipeline);
        return pipelineMapper.toDto(pipeline);
    }

    @Override
    public Optional<PipelineDTO> partialUpdate(PipelineDTO pipelineDTO) {
        log.debug("Request to partially update Pipeline : {}", pipelineDTO);

        return pipelineRepository
            .findById(pipelineDTO.getId())
            .map(existingPipeline -> {
                pipelineMapper.partialUpdate(existingPipeline, pipelineDTO);

                return existingPipeline;
            })
            .map(pipelineRepository::save)
            .map(pipelineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PipelineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pipelines");
        return pipelineRepository.findAll(pageable).map(pipelineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PipelineDTO> findOne(Long id) {
        log.debug("Request to get Pipeline : {}", id);
        return pipelineRepository.findById(id).map(pipelineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pipeline : {}", id);
        pipelineRepository.deleteById(id);
    }
}
