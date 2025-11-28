package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.Contact;
import com.mgs.repository.ContactRepository;
import com.mgs.service.criteria.ContactCriteria;
import com.mgs.service.dto.ContactDTO;
import com.mgs.service.mapper.ContactMapper;
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
 * Service for executing complex queries for {@link Contact} entities in the database.
 * The main input is a {@link ContactCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactQueryService extends QueryService<Contact> {

    private static final Logger LOG = LoggerFactory.getLogger(ContactQueryService.class);

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    public ContactQueryService(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    /**
     * Return a {@link Page} of {@link ContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactDTO> findByCriteria(ContactCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contact> specification = createSpecification(criteria);
        return contactRepository.findAll(specification, page).map(contactMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Contact> specification = createSpecification(criteria);
        return contactRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Contact> createSpecification(ContactCriteria criteria) {
        Specification<Contact> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Contact_.id),
                buildSpecification(criteria.getTenantId(), root -> root.join(Contact_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getCustomerId(), root -> root.join(Contact_.customer, JoinType.LEFT).get(Customer_.id)),
                buildSpecification(criteria.getAddressId(), root -> root.join(Contact_.address, JoinType.LEFT).get(Address_.id)),
                buildSpecification(criteria.getOwnerUserId(), root -> root.join(Contact_.ownerUser, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
