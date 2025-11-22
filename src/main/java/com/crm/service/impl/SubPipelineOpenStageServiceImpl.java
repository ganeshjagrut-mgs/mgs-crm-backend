package com.crm.service.impl;

import com.crm.domain.SubPipelineOpenStage;
import com.crm.repository.SubPipelineOpenStageRepository;
import com.crm.service.SubPipelineOpenStageService;
import com.crm.service.dto.SubPipelineOpenStageDTO;
import com.crm.service.mapper.SubPipelineOpenStageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.SubPipelineOpenStage}.
 */
@Service
@Transactional
public class SubPipelineOpenStageServiceImpl implements SubPipelineOpenStageService {

    private final Logger log = LoggerFactory.getLogger(SubPipelineOpenStageServiceImpl.class);

    private final SubPipelineOpenStageRepository subPipelineOpenStageRepository;

    private final SubPipelineOpenStageMapper subPipelineOpenStageMapper;

    public SubPipelineOpenStageServiceImpl(
        SubPipelineOpenStageRepository subPipelineOpenStageRepository,
        SubPipelineOpenStageMapper subPipelineOpenStageMapper
    ) {
        this.subPipelineOpenStageRepository = subPipelineOpenStageRepository;
        this.subPipelineOpenStageMapper = subPipelineOpenStageMapper;
    }

    @Override
    public SubPipelineOpenStageDTO save(SubPipelineOpenStageDTO subPipelineOpenStageDTO) {
        log.debug("Request to save SubPipelineOpenStage : {}", subPipelineOpenStageDTO);
        SubPipelineOpenStage subPipelineOpenStage = subPipelineOpenStageMapper.toEntity(subPipelineOpenStageDTO);
        subPipelineOpenStage = subPipelineOpenStageRepository.save(subPipelineOpenStage);
        return subPipelineOpenStageMapper.toDto(subPipelineOpenStage);
    }

    @Override
    public SubPipelineOpenStageDTO update(SubPipelineOpenStageDTO subPipelineOpenStageDTO) {
        log.debug("Request to update SubPipelineOpenStage : {}", subPipelineOpenStageDTO);
        SubPipelineOpenStage subPipelineOpenStage = subPipelineOpenStageMapper.toEntity(subPipelineOpenStageDTO);
        subPipelineOpenStage = subPipelineOpenStageRepository.save(subPipelineOpenStage);
        return subPipelineOpenStageMapper.toDto(subPipelineOpenStage);
    }

    @Override
    public Optional<SubPipelineOpenStageDTO> partialUpdate(SubPipelineOpenStageDTO subPipelineOpenStageDTO) {
        log.debug("Request to partially update SubPipelineOpenStage : {}", subPipelineOpenStageDTO);

        return subPipelineOpenStageRepository
            .findById(subPipelineOpenStageDTO.getId())
            .map(existingSubPipelineOpenStage -> {
                subPipelineOpenStageMapper.partialUpdate(existingSubPipelineOpenStage, subPipelineOpenStageDTO);

                return existingSubPipelineOpenStage;
            })
            .map(subPipelineOpenStageRepository::save)
            .map(subPipelineOpenStageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubPipelineOpenStageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubPipelineOpenStages");
        return subPipelineOpenStageRepository.findAll(pageable).map(subPipelineOpenStageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubPipelineOpenStageDTO> findOne(Long id) {
        log.debug("Request to get SubPipelineOpenStage : {}", id);
        return subPipelineOpenStageRepository.findById(id).map(subPipelineOpenStageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubPipelineOpenStage : {}", id);
        subPipelineOpenStageRepository.deleteById(id);
    }
}
