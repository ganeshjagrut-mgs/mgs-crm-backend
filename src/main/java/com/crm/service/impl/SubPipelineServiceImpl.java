package com.crm.service.impl;

import com.crm.domain.SubPipeline;
import com.crm.repository.SubPipelineRepository;
import com.crm.service.SubPipelineService;
import com.crm.service.dto.SubPipelineDTO;
import com.crm.service.mapper.SubPipelineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.SubPipeline}.
 */
@Service
@Transactional
public class SubPipelineServiceImpl implements SubPipelineService {

    private final Logger log = LoggerFactory.getLogger(SubPipelineServiceImpl.class);

    private final SubPipelineRepository subPipelineRepository;

    private final SubPipelineMapper subPipelineMapper;

    public SubPipelineServiceImpl(SubPipelineRepository subPipelineRepository, SubPipelineMapper subPipelineMapper) {
        this.subPipelineRepository = subPipelineRepository;
        this.subPipelineMapper = subPipelineMapper;
    }

    @Override
    public SubPipelineDTO save(SubPipelineDTO subPipelineDTO) {
        log.debug("Request to save SubPipeline : {}", subPipelineDTO);
        SubPipeline subPipeline = subPipelineMapper.toEntity(subPipelineDTO);
        subPipeline = subPipelineRepository.save(subPipeline);
        return subPipelineMapper.toDto(subPipeline);
    }

    @Override
    public SubPipelineDTO update(SubPipelineDTO subPipelineDTO) {
        log.debug("Request to update SubPipeline : {}", subPipelineDTO);
        SubPipeline subPipeline = subPipelineMapper.toEntity(subPipelineDTO);
        subPipeline = subPipelineRepository.save(subPipeline);
        return subPipelineMapper.toDto(subPipeline);
    }

    @Override
    public Optional<SubPipelineDTO> partialUpdate(SubPipelineDTO subPipelineDTO) {
        log.debug("Request to partially update SubPipeline : {}", subPipelineDTO);

        return subPipelineRepository
            .findById(subPipelineDTO.getId())
            .map(existingSubPipeline -> {
                subPipelineMapper.partialUpdate(existingSubPipeline, subPipelineDTO);

                return existingSubPipeline;
            })
            .map(subPipelineRepository::save)
            .map(subPipelineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubPipelineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubPipelines");
        return subPipelineRepository.findAll(pageable).map(subPipelineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubPipelineDTO> findOne(Long id) {
        log.debug("Request to get SubPipeline : {}", id);
        return subPipelineRepository.findById(id).map(subPipelineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubPipeline : {}", id);
        subPipelineRepository.deleteById(id);
    }
}
