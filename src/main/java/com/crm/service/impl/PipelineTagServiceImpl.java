package com.crm.service.impl;

import com.crm.domain.PipelineTag;
import com.crm.repository.PipelineTagRepository;
import com.crm.service.PipelineTagService;
import com.crm.service.dto.PipelineTagDTO;
import com.crm.service.mapper.PipelineTagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.crm.domain.PipelineTag}.
 */
@Service
@Transactional
public class PipelineTagServiceImpl implements PipelineTagService {

    private final Logger log = LoggerFactory.getLogger(PipelineTagServiceImpl.class);

    private final PipelineTagRepository pipelineTagRepository;

    private final PipelineTagMapper pipelineTagMapper;

    public PipelineTagServiceImpl(PipelineTagRepository pipelineTagRepository, PipelineTagMapper pipelineTagMapper) {
        this.pipelineTagRepository = pipelineTagRepository;
        this.pipelineTagMapper = pipelineTagMapper;
    }

    @Override
    public PipelineTagDTO save(PipelineTagDTO pipelineTagDTO) {
        log.debug("Request to save PipelineTag : {}", pipelineTagDTO);
        PipelineTag pipelineTag = pipelineTagMapper.toEntity(pipelineTagDTO);
        pipelineTag = pipelineTagRepository.save(pipelineTag);
        return pipelineTagMapper.toDto(pipelineTag);
    }

    @Override
    public PipelineTagDTO update(PipelineTagDTO pipelineTagDTO) {
        log.debug("Request to update PipelineTag : {}", pipelineTagDTO);
        PipelineTag pipelineTag = pipelineTagMapper.toEntity(pipelineTagDTO);
        pipelineTag = pipelineTagRepository.save(pipelineTag);
        return pipelineTagMapper.toDto(pipelineTag);
    }

    @Override
    public Optional<PipelineTagDTO> partialUpdate(PipelineTagDTO pipelineTagDTO) {
        log.debug("Request to partially update PipelineTag : {}", pipelineTagDTO);

        return pipelineTagRepository
            .findById(pipelineTagDTO.getId())
            .map(existingPipelineTag -> {
                pipelineTagMapper.partialUpdate(existingPipelineTag, pipelineTagDTO);

                return existingPipelineTag;
            })
            .map(pipelineTagRepository::save)
            .map(pipelineTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PipelineTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PipelineTags");
        return pipelineTagRepository.findAll(pageable).map(pipelineTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PipelineTagDTO> findOne(Long id) {
        log.debug("Request to get PipelineTag : {}", id);
        return pipelineTagRepository.findById(id).map(pipelineTagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PipelineTag : {}", id);
        pipelineTagRepository.deleteById(id);
    }
}
