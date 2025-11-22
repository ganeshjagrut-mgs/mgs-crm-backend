package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.Tenant;
import com.crm.repository.TenantRepository;
import com.crm.service.criteria.TenantCriteria;
import com.crm.service.dto.TenantDTO;
import com.crm.service.mapper.TenantMapper;
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
 * Service for executing complex queries for {@link Tenant} entities in the database.
 * The main input is a {@link TenantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TenantDTO} or a {@link Page} of {@link TenantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantQueryService extends QueryService<Tenant> {

    private final Logger log = LoggerFactory.getLogger(TenantQueryService.class);

    private final TenantRepository tenantRepository;

    private final TenantMapper tenantMapper;

    public TenantQueryService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    /**
     * Return a {@link List} of {@link TenantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TenantDTO> findByCriteria(TenantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tenant> specification = createSpecification(criteria);
        return tenantMapper.toDto(tenantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TenantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantDTO> findByCriteria(TenantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tenant> specification = createSpecification(criteria);
        return tenantRepository.findAll(specification, page).map(tenantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TenantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tenant> specification = createSpecification(criteria);
        return tenantRepository.count(specification);
    }

    /**
     * Function to convert {@link TenantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tenant> createSpecification(TenantCriteria criteria) {
        Specification<Tenant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tenant_.id));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), Tenant_.companyName));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), Tenant_.contactPerson));
            }
            if (criteria.getLogo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogo(), Tenant_.logo));
            }
            if (criteria.getWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsite(), Tenant_.website));
            }
            if (criteria.getRegistrationNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegistrationNumber(), Tenant_.registrationNumber));
            }
            if (criteria.getSubId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubId(), Tenant_.subId));
            }
            if (criteria.getAddressesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAddressesId(), root -> root.join(Tenant_.addresses, JoinType.LEFT).get(Address_.id))
                    );
            }
            if (criteria.getUsersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsersId(), root -> root.join(Tenant_.users, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getEncryptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEncryptionId(),
                            root -> root.join(Tenant_.encryption, JoinType.LEFT).get(Encryption_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
