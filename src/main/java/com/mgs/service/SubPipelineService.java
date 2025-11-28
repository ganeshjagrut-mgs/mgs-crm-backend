package com.mgs.service;

import com.mgs.domain.SubPipeline;
import com.mgs.repository.SubPipelineRepository;
import com.mgs.service.dto.SubPipelineDTO;
import com.mgs.service.mapper.SubPipelineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.SubPipeline}.
 */
@Service
@Transactional
public class SubPipelineService {

    private static final Logger LOG = LoggerFactory.getLogger(SubPipelineService.class);

    private final SubPipelineRepository subPipelineRepository;

    private final SubPipelineMapper subPipelineMapper;

    public SubPipelineService(SubPipelineRepository subPipelineRepository, SubPipelineMapper subPipelineMapper) {
        this.subPipelineRepository = subPipelineRepository;
        this.subPipelineMapper = subPipelineMapper;
    }

    /**
     * Save a subPipeline.
     *
     * @param subPipelineDTO the entity to save.
     * @return the persisted entity.
     */
    public SubPipelineDTO save(SubPipelineDTO subPipelineDTO) {
        LOG.debug("Request to save SubPipeline : {}", subPipelineDTO);
        SubPipeline subPipeline = subPipelineMapper.toEntity(subPipelineDTO);
        subPipeline = subPipelineRepository.save(subPipeline);
        return subPipelineMapper.toDto(subPipeline);
    }

    /**
     * Update a subPipeline.
     *
     * @param subPipelineDTO the entity to save.
     * @return the persisted entity.
     */
    public SubPipelineDTO update(SubPipelineDTO subPipelineDTO) {
        LOG.debug("Request to update SubPipeline : {}", subPipelineDTO);
        SubPipeline subPipeline = subPipelineMapper.toEntity(subPipelineDTO);
        subPipeline = subPipelineRepository.save(subPipeline);
        return subPipelineMapper.toDto(subPipeline);
    }

    /**
     * Partially update a subPipeline.
     *
     * @param subPipelineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubPipelineDTO> partialUpdate(SubPipelineDTO subPipelineDTO) {
        LOG.debug("Request to partially update SubPipeline : {}", subPipelineDTO);

        return subPipelineRepository
            .findById(subPipelineDTO.getId())
            .map(existingSubPipeline -> {
                subPipelineMapper.partialUpdate(existingSubPipeline, subPipelineDTO);

                return existingSubPipeline;
            })
            .map(subPipelineRepository::save)
            .map(subPipelineMapper::toDto);
    }

    /**
     * Get one subPipeline by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubPipelineDTO> findOne(Long id) {
        LOG.debug("Request to get SubPipeline : {}", id);
        return subPipelineRepository.findById(id).map(subPipelineMapper::toDto);
    }

    /**
     * Delete the subPipeline by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SubPipeline : {}", id);
        subPipelineRepository.deleteById(id);
    }
}
