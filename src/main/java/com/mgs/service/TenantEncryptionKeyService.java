package com.mgs.service;

import com.mgs.domain.TenantEncryptionKey;
import com.mgs.repository.TenantEncryptionKeyRepository;
import com.mgs.service.dto.TenantEncryptionKeyDTO;
import com.mgs.service.mapper.TenantEncryptionKeyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.TenantEncryptionKey}.
 */
@Service
@Transactional
public class TenantEncryptionKeyService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantEncryptionKeyService.class);

    private final TenantEncryptionKeyRepository tenantEncryptionKeyRepository;

    private final TenantEncryptionKeyMapper tenantEncryptionKeyMapper;

    public TenantEncryptionKeyService(
        TenantEncryptionKeyRepository tenantEncryptionKeyRepository,
        TenantEncryptionKeyMapper tenantEncryptionKeyMapper
    ) {
        this.tenantEncryptionKeyRepository = tenantEncryptionKeyRepository;
        this.tenantEncryptionKeyMapper = tenantEncryptionKeyMapper;
    }

    /**
     * Save a tenantEncryptionKey.
     *
     * @param tenantEncryptionKeyDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantEncryptionKeyDTO save(TenantEncryptionKeyDTO tenantEncryptionKeyDTO) {
        LOG.debug("Request to save TenantEncryptionKey : {}", tenantEncryptionKeyDTO);
        TenantEncryptionKey tenantEncryptionKey = tenantEncryptionKeyMapper.toEntity(tenantEncryptionKeyDTO);
        tenantEncryptionKey = tenantEncryptionKeyRepository.save(tenantEncryptionKey);
        return tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);
    }

    /**
     * Update a tenantEncryptionKey.
     *
     * @param tenantEncryptionKeyDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantEncryptionKeyDTO update(TenantEncryptionKeyDTO tenantEncryptionKeyDTO) {
        LOG.debug("Request to update TenantEncryptionKey : {}", tenantEncryptionKeyDTO);
        TenantEncryptionKey tenantEncryptionKey = tenantEncryptionKeyMapper.toEntity(tenantEncryptionKeyDTO);
        tenantEncryptionKey = tenantEncryptionKeyRepository.save(tenantEncryptionKey);
        return tenantEncryptionKeyMapper.toDto(tenantEncryptionKey);
    }

    /**
     * Partially update a tenantEncryptionKey.
     *
     * @param tenantEncryptionKeyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TenantEncryptionKeyDTO> partialUpdate(TenantEncryptionKeyDTO tenantEncryptionKeyDTO) {
        LOG.debug("Request to partially update TenantEncryptionKey : {}", tenantEncryptionKeyDTO);

        return tenantEncryptionKeyRepository
            .findById(tenantEncryptionKeyDTO.getId())
            .map(existingTenantEncryptionKey -> {
                tenantEncryptionKeyMapper.partialUpdate(existingTenantEncryptionKey, tenantEncryptionKeyDTO);

                return existingTenantEncryptionKey;
            })
            .map(tenantEncryptionKeyRepository::save)
            .map(tenantEncryptionKeyMapper::toDto);
    }

    /**
     * Get one tenantEncryptionKey by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TenantEncryptionKeyDTO> findOne(Long id) {
        LOG.debug("Request to get TenantEncryptionKey : {}", id);
        return tenantEncryptionKeyRepository.findById(id).map(tenantEncryptionKeyMapper::toDto);
    }

    /**
     * Delete the tenantEncryptionKey by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TenantEncryptionKey : {}", id);
        tenantEncryptionKeyRepository.deleteById(id);
    }
}
