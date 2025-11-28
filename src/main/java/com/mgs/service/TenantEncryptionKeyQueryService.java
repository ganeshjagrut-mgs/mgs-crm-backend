package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.TenantEncryptionKey;
import com.mgs.repository.TenantEncryptionKeyRepository;
import com.mgs.service.criteria.TenantEncryptionKeyCriteria;
import com.mgs.service.dto.TenantEncryptionKeyDTO;
import com.mgs.service.mapper.TenantEncryptionKeyMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TenantEncryptionKey} entities in the database.
 * The main input is a {@link TenantEncryptionKeyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TenantEncryptionKeyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantEncryptionKeyQueryService extends QueryService<TenantEncryptionKey> {

    private static final Logger LOG = LoggerFactory.getLogger(TenantEncryptionKeyQueryService.class);

    private final TenantEncryptionKeyRepository tenantEncryptionKeyRepository;

    private final TenantEncryptionKeyMapper tenantEncryptionKeyMapper;

    public TenantEncryptionKeyQueryService(
        TenantEncryptionKeyRepository tenantEncryptionKeyRepository,
        TenantEncryptionKeyMapper tenantEncryptionKeyMapper
    ) {
        this.tenantEncryptionKeyRepository = tenantEncryptionKeyRepository;
        this.tenantEncryptionKeyMapper = tenantEncryptionKeyMapper;
    }

    /**
     * Return a {@link Page} of {@link TenantEncryptionKeyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantEncryptionKeyDTO> findByCriteria(TenantEncryptionKeyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TenantEncryptionKey> specification = createSpecification(criteria);
        return tenantEncryptionKeyRepository.findAll(specification, page).map(tenantEncryptionKeyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TenantEncryptionKeyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TenantEncryptionKey> specification = createSpecification(criteria);
        return tenantEncryptionKeyRepository.count(specification);
    }

    /**
     * Function to convert {@link TenantEncryptionKeyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TenantEncryptionKey> createSpecification(TenantEncryptionKeyCriteria criteria) {
        Specification<TenantEncryptionKey> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TenantEncryptionKey_.id),
                buildRangeSpecification(criteria.getKeyVersion(), TenantEncryptionKey_.keyVersion),
                buildStringSpecification(criteria.getEncryptedDataKey(), TenantEncryptionKey_.encryptedDataKey),
                buildStringSpecification(criteria.getPinHash(), TenantEncryptionKey_.pinHash),
                buildStringSpecification(criteria.getPinSalt(), TenantEncryptionKey_.pinSalt),
                buildSpecification(criteria.getIsActive(), TenantEncryptionKey_.isActive),
                buildSpecification(criteria.getTenantId(), root -> root.join(TenantEncryptionKey_.tenant, JoinType.LEFT).get(Tenant_.id))
            );
        }
        return specification;
    }
}
