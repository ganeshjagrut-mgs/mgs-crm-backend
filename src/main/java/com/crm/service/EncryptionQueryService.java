package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.Encryption;
import com.crm.repository.EncryptionRepository;
import com.crm.service.criteria.EncryptionCriteria;
import com.crm.service.dto.EncryptionDTO;
import com.crm.service.mapper.EncryptionMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Encryption} entities in the database.
 * The main input is a {@link EncryptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EncryptionDTO} or a {@link Page} of {@link EncryptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EncryptionQueryService extends QueryService<Encryption> {

    private final Logger log = LoggerFactory.getLogger(EncryptionQueryService.class);

    private final EncryptionRepository encryptionRepository;

    private final EncryptionMapper encryptionMapper;

    public EncryptionQueryService(EncryptionRepository encryptionRepository, EncryptionMapper encryptionMapper) {
        this.encryptionRepository = encryptionRepository;
        this.encryptionMapper = encryptionMapper;
    }

    /**
     * Return a {@link List} of {@link EncryptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EncryptionDTO> findByCriteria(EncryptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Encryption> specification = createSpecification(criteria);
        return encryptionMapper.toDto(encryptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EncryptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EncryptionDTO> findByCriteria(EncryptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Encryption> specification = createSpecification(criteria);
        return encryptionRepository.findAll(specification, page).map(encryptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EncryptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Encryption> specification = createSpecification(criteria);
        return encryptionRepository.count(specification);
    }

    /**
     * Function to convert {@link EncryptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Encryption> createSpecification(EncryptionCriteria criteria) {
        Specification<Encryption> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Encryption_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), Encryption_.key));
            }
            if (criteria.getPin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPin(), Encryption_.pin));
            }
            if (criteria.getTenantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTenantId(), root -> root.join(Encryption_.tenant, JoinType.LEFT).get(Tenant_.id))
                    );
            }
        }
        return specification;
    }
}
