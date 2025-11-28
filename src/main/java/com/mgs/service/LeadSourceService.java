package com.mgs.service;

import com.mgs.domain.LeadSource;
import com.mgs.repository.LeadSourceRepository;
import com.mgs.service.dto.LeadSourceDTO;
import com.mgs.service.mapper.LeadSourceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.LeadSource}.
 */
@Service
@Transactional
public class LeadSourceService {

    private static final Logger LOG = LoggerFactory.getLogger(LeadSourceService.class);

    private final LeadSourceRepository leadSourceRepository;

    private final LeadSourceMapper leadSourceMapper;

    public LeadSourceService(LeadSourceRepository leadSourceRepository, LeadSourceMapper leadSourceMapper) {
        this.leadSourceRepository = leadSourceRepository;
        this.leadSourceMapper = leadSourceMapper;
    }

    /**
     * Save a leadSource.
     *
     * @param leadSourceDTO the entity to save.
     * @return the persisted entity.
     */
    public LeadSourceDTO save(LeadSourceDTO leadSourceDTO) {
        LOG.debug("Request to save LeadSource : {}", leadSourceDTO);
        LeadSource leadSource = leadSourceMapper.toEntity(leadSourceDTO);
        leadSource = leadSourceRepository.save(leadSource);
        return leadSourceMapper.toDto(leadSource);
    }

    /**
     * Update a leadSource.
     *
     * @param leadSourceDTO the entity to save.
     * @return the persisted entity.
     */
    public LeadSourceDTO update(LeadSourceDTO leadSourceDTO) {
        LOG.debug("Request to update LeadSource : {}", leadSourceDTO);
        LeadSource leadSource = leadSourceMapper.toEntity(leadSourceDTO);
        leadSource = leadSourceRepository.save(leadSource);
        return leadSourceMapper.toDto(leadSource);
    }

    /**
     * Partially update a leadSource.
     *
     * @param leadSourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LeadSourceDTO> partialUpdate(LeadSourceDTO leadSourceDTO) {
        LOG.debug("Request to partially update LeadSource : {}", leadSourceDTO);

        return leadSourceRepository
            .findById(leadSourceDTO.getId())
            .map(existingLeadSource -> {
                leadSourceMapper.partialUpdate(existingLeadSource, leadSourceDTO);

                return existingLeadSource;
            })
            .map(leadSourceRepository::save)
            .map(leadSourceMapper::toDto);
    }

    /**
     * Get one leadSource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeadSourceDTO> findOne(Long id) {
        LOG.debug("Request to get LeadSource : {}", id);
        return leadSourceRepository.findById(id).map(leadSourceMapper::toDto);
    }

    /**
     * Delete the leadSource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LeadSource : {}", id);
        leadSourceRepository.deleteById(id);
    }
}
