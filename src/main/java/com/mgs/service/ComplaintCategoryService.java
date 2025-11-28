package com.mgs.service;

import com.mgs.domain.ComplaintCategory;
import com.mgs.repository.ComplaintCategoryRepository;
import com.mgs.service.dto.ComplaintCategoryDTO;
import com.mgs.service.mapper.ComplaintCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.ComplaintCategory}.
 */
@Service
@Transactional
public class ComplaintCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintCategoryService.class);

    private final ComplaintCategoryRepository complaintCategoryRepository;

    private final ComplaintCategoryMapper complaintCategoryMapper;

    public ComplaintCategoryService(
        ComplaintCategoryRepository complaintCategoryRepository,
        ComplaintCategoryMapper complaintCategoryMapper
    ) {
        this.complaintCategoryRepository = complaintCategoryRepository;
        this.complaintCategoryMapper = complaintCategoryMapper;
    }

    /**
     * Save a complaintCategory.
     *
     * @param complaintCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ComplaintCategoryDTO save(ComplaintCategoryDTO complaintCategoryDTO) {
        LOG.debug("Request to save ComplaintCategory : {}", complaintCategoryDTO);
        ComplaintCategory complaintCategory = complaintCategoryMapper.toEntity(complaintCategoryDTO);
        complaintCategory = complaintCategoryRepository.save(complaintCategory);
        return complaintCategoryMapper.toDto(complaintCategory);
    }

    /**
     * Update a complaintCategory.
     *
     * @param complaintCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ComplaintCategoryDTO update(ComplaintCategoryDTO complaintCategoryDTO) {
        LOG.debug("Request to update ComplaintCategory : {}", complaintCategoryDTO);
        ComplaintCategory complaintCategory = complaintCategoryMapper.toEntity(complaintCategoryDTO);
        complaintCategory = complaintCategoryRepository.save(complaintCategory);
        return complaintCategoryMapper.toDto(complaintCategory);
    }

    /**
     * Partially update a complaintCategory.
     *
     * @param complaintCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ComplaintCategoryDTO> partialUpdate(ComplaintCategoryDTO complaintCategoryDTO) {
        LOG.debug("Request to partially update ComplaintCategory : {}", complaintCategoryDTO);

        return complaintCategoryRepository
            .findById(complaintCategoryDTO.getId())
            .map(existingComplaintCategory -> {
                complaintCategoryMapper.partialUpdate(existingComplaintCategory, complaintCategoryDTO);

                return existingComplaintCategory;
            })
            .map(complaintCategoryRepository::save)
            .map(complaintCategoryMapper::toDto);
    }

    /**
     * Get one complaintCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComplaintCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get ComplaintCategory : {}", id);
        return complaintCategoryRepository.findById(id).map(complaintCategoryMapper::toDto);
    }

    /**
     * Delete the complaintCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ComplaintCategory : {}", id);
        complaintCategoryRepository.deleteById(id);
    }
}
