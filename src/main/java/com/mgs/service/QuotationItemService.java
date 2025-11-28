package com.mgs.service;

import com.mgs.domain.QuotationItem;
import com.mgs.repository.QuotationItemRepository;
import com.mgs.service.dto.QuotationItemDTO;
import com.mgs.service.mapper.QuotationItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.QuotationItem}.
 */
@Service
@Transactional
public class QuotationItemService {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationItemService.class);

    private final QuotationItemRepository quotationItemRepository;

    private final QuotationItemMapper quotationItemMapper;

    public QuotationItemService(QuotationItemRepository quotationItemRepository, QuotationItemMapper quotationItemMapper) {
        this.quotationItemRepository = quotationItemRepository;
        this.quotationItemMapper = quotationItemMapper;
    }

    /**
     * Save a quotationItem.
     *
     * @param quotationItemDTO the entity to save.
     * @return the persisted entity.
     */
    public QuotationItemDTO save(QuotationItemDTO quotationItemDTO) {
        LOG.debug("Request to save QuotationItem : {}", quotationItemDTO);
        QuotationItem quotationItem = quotationItemMapper.toEntity(quotationItemDTO);
        quotationItem = quotationItemRepository.save(quotationItem);
        return quotationItemMapper.toDto(quotationItem);
    }

    /**
     * Update a quotationItem.
     *
     * @param quotationItemDTO the entity to save.
     * @return the persisted entity.
     */
    public QuotationItemDTO update(QuotationItemDTO quotationItemDTO) {
        LOG.debug("Request to update QuotationItem : {}", quotationItemDTO);
        QuotationItem quotationItem = quotationItemMapper.toEntity(quotationItemDTO);
        quotationItem = quotationItemRepository.save(quotationItem);
        return quotationItemMapper.toDto(quotationItem);
    }

    /**
     * Partially update a quotationItem.
     *
     * @param quotationItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuotationItemDTO> partialUpdate(QuotationItemDTO quotationItemDTO) {
        LOG.debug("Request to partially update QuotationItem : {}", quotationItemDTO);

        return quotationItemRepository
            .findById(quotationItemDTO.getId())
            .map(existingQuotationItem -> {
                quotationItemMapper.partialUpdate(existingQuotationItem, quotationItemDTO);

                return existingQuotationItem;
            })
            .map(quotationItemRepository::save)
            .map(quotationItemMapper::toDto);
    }

    /**
     * Get one quotationItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuotationItemDTO> findOne(Long id) {
        LOG.debug("Request to get QuotationItem : {}", id);
        return quotationItemRepository.findById(id).map(quotationItemMapper::toDto);
    }

    /**
     * Delete the quotationItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QuotationItem : {}", id);
        quotationItemRepository.deleteById(id);
    }
}
