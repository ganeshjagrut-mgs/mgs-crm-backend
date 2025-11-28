package com.mgs.service;

import com.mgs.domain.Deal;
import com.mgs.repository.DealRepository;
import com.mgs.service.dto.DealDTO;
import com.mgs.service.mapper.DealMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.Deal}.
 */
@Service
@Transactional
public class DealService {

    private static final Logger LOG = LoggerFactory.getLogger(DealService.class);

    private final DealRepository dealRepository;

    private final DealMapper dealMapper;

    public DealService(DealRepository dealRepository, DealMapper dealMapper) {
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
    }

    /**
     * Save a deal.
     *
     * @param dealDTO the entity to save.
     * @return the persisted entity.
     */
    public DealDTO save(DealDTO dealDTO) {
        LOG.debug("Request to save Deal : {}", dealDTO);
        Deal deal = dealMapper.toEntity(dealDTO);
        deal = dealRepository.save(deal);
        return dealMapper.toDto(deal);
    }

    /**
     * Update a deal.
     *
     * @param dealDTO the entity to save.
     * @return the persisted entity.
     */
    public DealDTO update(DealDTO dealDTO) {
        LOG.debug("Request to update Deal : {}", dealDTO);
        Deal deal = dealMapper.toEntity(dealDTO);
        deal = dealRepository.save(deal);
        return dealMapper.toDto(deal);
    }

    /**
     * Partially update a deal.
     *
     * @param dealDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DealDTO> partialUpdate(DealDTO dealDTO) {
        LOG.debug("Request to partially update Deal : {}", dealDTO);

        return dealRepository
            .findById(dealDTO.getId())
            .map(existingDeal -> {
                dealMapper.partialUpdate(existingDeal, dealDTO);

                return existingDeal;
            })
            .map(dealRepository::save)
            .map(dealMapper::toDto);
    }

    /**
     * Get one deal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DealDTO> findOne(Long id) {
        LOG.debug("Request to get Deal : {}", id);
        return dealRepository.findById(id).map(dealMapper::toDto);
    }

    /**
     * Delete the deal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Deal : {}", id);
        dealRepository.deleteById(id);
    }
}
