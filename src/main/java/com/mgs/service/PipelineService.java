package com.mgs.service;

import com.mgs.domain.Pipeline;
import com.mgs.repository.PipelineRepository;
import com.mgs.service.dto.PipelineDTO;
import com.mgs.service.mapper.PipelineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.Pipeline}.
 */
@Service
@Transactional
public class PipelineService {

    private static final Logger LOG = LoggerFactory.getLogger(PipelineService.class);

    private final PipelineRepository pipelineRepository;

    private final PipelineMapper pipelineMapper;

    public PipelineService(PipelineRepository pipelineRepository, PipelineMapper pipelineMapper) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineMapper = pipelineMapper;
    }

    /**
     * Save a pipeline.
     *
     * @param pipelineDTO the entity to save.
     * @return the persisted entity.
     */
    public PipelineDTO save(PipelineDTO pipelineDTO) {
        LOG.debug("Request to save Pipeline : {}", pipelineDTO);
        Pipeline pipeline = pipelineMapper.toEntity(pipelineDTO);
        pipeline = pipelineRepository.save(pipeline);
        return pipelineMapper.toDto(pipeline);
    }

    /**
     * Update a pipeline.
     *
     * @param pipelineDTO the entity to save.
     * @return the persisted entity.
     */
    public PipelineDTO update(PipelineDTO pipelineDTO) {
        LOG.debug("Request to update Pipeline : {}", pipelineDTO);
        Pipeline pipeline = pipelineMapper.toEntity(pipelineDTO);
        pipeline = pipelineRepository.save(pipeline);
        return pipelineMapper.toDto(pipeline);
    }

    /**
     * Partially update a pipeline.
     *
     * @param pipelineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PipelineDTO> partialUpdate(PipelineDTO pipelineDTO) {
        LOG.debug("Request to partially update Pipeline : {}", pipelineDTO);

        return pipelineRepository
            .findById(pipelineDTO.getId())
            .map(existingPipeline -> {
                pipelineMapper.partialUpdate(existingPipeline, pipelineDTO);

                return existingPipeline;
            })
            .map(pipelineRepository::save)
            .map(pipelineMapper::toDto);
    }

    /**
     * Get one pipeline by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PipelineDTO> findOne(Long id) {
        LOG.debug("Request to get Pipeline : {}", id);
        return pipelineRepository.findById(id).map(pipelineMapper::toDto);
    }

    /**
     * Delete the pipeline by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Pipeline : {}", id);
        pipelineRepository.deleteById(id);
    }
}
