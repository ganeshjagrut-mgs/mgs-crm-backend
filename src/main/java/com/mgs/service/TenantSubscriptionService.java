package com.mgs.service;

import com.mgs.domain.TenantSubscription;
import com.mgs.repository.TenantSubscriptionRepository;
import com.mgs.service.dto.TenantSubscriptionDTO;
import com.mgs.service.mapper.TenantSubscriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.TenantSubscription}.
 */
@Service
@Transactional
public class TenantSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantSubscriptionService.class);

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    private final TenantSubscriptionMapper tenantSubscriptionMapper;

    public TenantSubscriptionService(
        TenantSubscriptionRepository tenantSubscriptionRepository,
        TenantSubscriptionMapper tenantSubscriptionMapper
    ) {
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.tenantSubscriptionMapper = tenantSubscriptionMapper;
    }

    /**
     * Save a tenantSubscription.
     *
     * @param tenantSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantSubscriptionDTO save(TenantSubscriptionDTO tenantSubscriptionDTO) {
        LOG.debug("Request to save TenantSubscription : {}", tenantSubscriptionDTO);
        TenantSubscription tenantSubscription = tenantSubscriptionMapper.toEntity(tenantSubscriptionDTO);
        tenantSubscription = tenantSubscriptionRepository.save(tenantSubscription);
        return tenantSubscriptionMapper.toDto(tenantSubscription);
    }

    /**
     * Update a tenantSubscription.
     *
     * @param tenantSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantSubscriptionDTO update(TenantSubscriptionDTO tenantSubscriptionDTO) {
        LOG.debug("Request to update TenantSubscription : {}", tenantSubscriptionDTO);
        TenantSubscription tenantSubscription = tenantSubscriptionMapper.toEntity(tenantSubscriptionDTO);
        tenantSubscription = tenantSubscriptionRepository.save(tenantSubscription);
        return tenantSubscriptionMapper.toDto(tenantSubscription);
    }

    /**
     * Partially update a tenantSubscription.
     *
     * @param tenantSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TenantSubscriptionDTO> partialUpdate(TenantSubscriptionDTO tenantSubscriptionDTO) {
        LOG.debug("Request to partially update TenantSubscription : {}", tenantSubscriptionDTO);

        return tenantSubscriptionRepository
            .findById(tenantSubscriptionDTO.getId())
            .map(existingTenantSubscription -> {
                tenantSubscriptionMapper.partialUpdate(existingTenantSubscription, tenantSubscriptionDTO);

                return existingTenantSubscription;
            })
            .map(tenantSubscriptionRepository::save)
            .map(tenantSubscriptionMapper::toDto);
    }

    /**
     * Get one tenantSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TenantSubscriptionDTO> findOne(Long id) {
        LOG.debug("Request to get TenantSubscription : {}", id);
        return tenantSubscriptionRepository.findById(id).map(tenantSubscriptionMapper::toDto);
    }

    /**
     * Delete the tenantSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TenantSubscription : {}", id);
        tenantSubscriptionRepository.deleteById(id);
    }
}
