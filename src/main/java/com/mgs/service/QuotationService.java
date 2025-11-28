package com.mgs.service;

import com.mgs.domain.Quotation;
import com.mgs.repository.QuotationRepository;
import com.mgs.service.dto.QuotationDTO;
import com.mgs.service.mapper.QuotationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.Quotation}.
 */
@Service
@Transactional
public class QuotationService {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationService.class);

    private final QuotationRepository quotationRepository;

    private final QuotationMapper quotationMapper;

    public QuotationService(QuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    /**
     * Save a quotation.
     *
     * @param quotationDTO the entity to save.
     * @return the persisted entity.
     */
    public QuotationDTO save(QuotationDTO quotationDTO) {
        LOG.debug("Request to save Quotation : {}", quotationDTO);
        Quotation quotation = quotationMapper.toEntity(quotationDTO);
        quotation = quotationRepository.save(quotation);
        return quotationMapper.toDto(quotation);
    }

    /**
     * Update a quotation.
     *
     * @param quotationDTO the entity to save.
     * @return the persisted entity.
     */
    public QuotationDTO update(QuotationDTO quotationDTO) {
        LOG.debug("Request to update Quotation : {}", quotationDTO);
        Quotation quotation = quotationMapper.toEntity(quotationDTO);
        quotation = quotationRepository.save(quotation);
        return quotationMapper.toDto(quotation);
    }

    /**
     * Partially update a quotation.
     *
     * @param quotationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuotationDTO> partialUpdate(QuotationDTO quotationDTO) {
        LOG.debug("Request to partially update Quotation : {}", quotationDTO);

        return quotationRepository
            .findById(quotationDTO.getId())
            .map(existingQuotation -> {
                quotationMapper.partialUpdate(existingQuotation, quotationDTO);

                return existingQuotation;
            })
            .map(quotationRepository::save)
            .map(quotationMapper::toDto);
    }

    /**
     * Get one quotation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuotationDTO> findOne(Long id) {
        LOG.debug("Request to get Quotation : {}", id);
        return quotationRepository.findById(id).map(quotationMapper::toDto);
    }

    /**
     * Delete the quotation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Quotation : {}", id);
        quotationRepository.deleteById(id);
    }
}
