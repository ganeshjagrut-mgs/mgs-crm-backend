package com.crm.service.impl;

import com.crm.domain.SubPipelineCloseStage;
import com.crm.repository.SubPipelineCloseStageRepository;
import com.crm.service.SubPipelineCloseStageService;
import com.crm.service.dto.SubPipelineCloseStageDTO;
import com.crm.service.mapper.SubPipelineCloseStageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.SubPipelineCloseStage}.
 */
@Service
@Transactional
public class SubPipelineCloseStageServiceImpl implements SubPipelineCloseStageService {

    private final Logger log = LoggerFactory.getLogger(SubPipelineCloseStageServiceImpl.class);

    private final SubPipelineCloseStageRepository subPipelineCloseStageRepository;

    private final SubPipelineCloseStageMapper subPipelineCloseStageMapper;

    public SubPipelineCloseStageServiceImpl(
        SubPipelineCloseStageRepository subPipelineCloseStageRepository,
        SubPipelineCloseStageMapper subPipelineCloseStageMapper
    ) {
        this.subPipelineCloseStageRepository = subPipelineCloseStageRepository;
        this.subPipelineCloseStageMapper = subPipelineCloseStageMapper;
    }

    @Override
    public SubPipelineCloseStageDTO save(SubPipelineCloseStageDTO subPipelineCloseStageDTO) {
        log.debug("Request to save SubPipelineCloseStage : {}", subPipelineCloseStageDTO);
        SubPipelineCloseStage subPipelineCloseStage = subPipelineCloseStageMapper.toEntity(subPipelineCloseStageDTO);
        subPipelineCloseStage = subPipelineCloseStageRepository.save(subPipelineCloseStage);
        return subPipelineCloseStageMapper.toDto(subPipelineCloseStage);
    }

    @Override
    public SubPipelineCloseStageDTO update(SubPipelineCloseStageDTO subPipelineCloseStageDTO) {
        log.debug("Request to update SubPipelineCloseStage : {}", subPipelineCloseStageDTO);
        SubPipelineCloseStage subPipelineCloseStage = subPipelineCloseStageMapper.toEntity(subPipelineCloseStageDTO);
        subPipelineCloseStage = subPipelineCloseStageRepository.save(subPipelineCloseStage);
        return subPipelineCloseStageMapper.toDto(subPipelineCloseStage);
    }

    @Override
    public Optional<SubPipelineCloseStageDTO> partialUpdate(SubPipelineCloseStageDTO subPipelineCloseStageDTO) {
        log.debug("Request to partially update SubPipelineCloseStage : {}", subPipelineCloseStageDTO);

        return subPipelineCloseStageRepository
            .findById(subPipelineCloseStageDTO.getId())
            .map(existingSubPipelineCloseStage -> {
                subPipelineCloseStageMapper.partialUpdate(existingSubPipelineCloseStage, subPipelineCloseStageDTO);

                return existingSubPipelineCloseStage;
            })
            .map(subPipelineCloseStageRepository::save)
            .map(subPipelineCloseStageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubPipelineCloseStageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubPipelineCloseStages");
        return subPipelineCloseStageRepository.findAll(pageable).map(subPipelineCloseStageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubPipelineCloseStageDTO> findOne(Long id) {
        log.debug("Request to get SubPipelineCloseStage : {}", id);
        return subPipelineCloseStageRepository.findById(id).map(subPipelineCloseStageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubPipelineCloseStage : {}", id);
        subPipelineCloseStageRepository.deleteById(id);
    }
}
