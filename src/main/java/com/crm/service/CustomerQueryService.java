package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.Customer;
import com.crm.repository.CustomerRepository;
import com.crm.service.criteria.CustomerCriteria;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.mapper.CustomerMapper;
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
 * Service for executing complex queries for {@link Customer} entities in the database.
 * The main input is a {@link CustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerDTO} or a {@link Page} of {@link CustomerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerQueryService extends QueryService<Customer> {

    private final Logger log = LoggerFactory.getLogger(CustomerQueryService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerQueryService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    /**
     * Return a {@link List} of {@link CustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> findByCriteria(CustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerMapper.toDto(customerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO> findByCriteria(CustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification, page).map(customerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Customer> createSpecification(CustomerCriteria criteria) {
        Specification<Customer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Customer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Customer_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Customer_.description));
            }
            if (criteria.getCompanyCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyCity(), Customer_.companyCity));
            }
            if (criteria.getCompanyArea() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyArea(), Customer_.companyArea));
            }
            if (criteria.getWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsite(), Customer_.website));
            }
            if (criteria.getCustomerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerName(), Customer_.customerName));
            }
            if (criteria.getCurrencyType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyType(), Customer_.currencyType));
            }
            if (criteria.getMaxInvoiceAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxInvoiceAmount(), Customer_.maxInvoiceAmount));
            }
            if (criteria.getGstNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGstNo(), Customer_.gstNo));
            }
            if (criteria.getPanNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPanNo(), Customer_.panNo));
            }
            if (criteria.getServiceTaxNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceTaxNo(), Customer_.serviceTaxNo));
            }
            if (criteria.getTanNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTanNo(), Customer_.tanNo));
            }
            if (criteria.getCorrelationId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrelationId(), Customer_.correlationId));
            }
            if (criteria.getAccountNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountNo(), Customer_.accountNo));
            }
            if (criteria.getGstStateName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGstStateName(), Customer_.gstStateName));
            }
            if (criteria.getGstStateCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGstStateCode(), Customer_.gstStateCode));
            }
            if (criteria.getIsSubmitSampleWithoutPO() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getIsSubmitSampleWithoutPO(), Customer_.isSubmitSampleWithoutPO));
            }
            if (criteria.getIsBlock() != null) {
                specification = specification.and(buildSpecification(criteria.getIsBlock(), Customer_.isBlock));
            }
            if (criteria.getAccountType() != null) {
                specification = specification.and(buildSpecification(criteria.getAccountType(), Customer_.accountType));
            }
            if (criteria.getAccountManagement() != null) {
                specification = specification.and(buildSpecification(criteria.getAccountManagement(), Customer_.accountManagement));
            }
            if (criteria.getRevenuePotential() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRevenuePotential(), Customer_.revenuePotential));
            }
            if (criteria.getSamplePotential() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSamplePotential(), Customer_.samplePotential));
            }
            if (criteria.getTotalPipeline() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPipeline(), Customer_.totalPipeline));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Customer_.type));
            }
            if (criteria.getAddressesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAddressesId(),
                            root -> root.join(Customer_.addresses, JoinType.LEFT).get(Address_.id)
                        )
                    );
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(Customer_.company, JoinType.LEFT).get(CustomerCompany_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Customer_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getCustomerTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerTypeId(),
                            root -> root.join(Customer_.customerType, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getCustomerStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerStatusId(),
                            root -> root.join(Customer_.customerStatus, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getOwnershipTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOwnershipTypeId(),
                            root -> root.join(Customer_.ownershipType, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getIndustryTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIndustryTypeId(),
                            root -> root.join(Customer_.industryType, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getCustomerCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerCategoryId(),
                            root -> root.join(Customer_.customerCategory, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getPaymentTermsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentTermsId(),
                            root -> root.join(Customer_.paymentTerms, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getInvoiceFrequencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInvoiceFrequencyId(),
                            root -> root.join(Customer_.invoiceFrequency, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getGstTreatmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGstTreatmentId(),
                            root -> root.join(Customer_.gstTreatment, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getOutstandingPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOutstandingPersonId(),
                            root -> root.join(Customer_.outstandingPerson, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(Customer_.department, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
            if (criteria.getTenatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTenatId(), root -> root.join(Customer_.tenat, JoinType.LEFT).get(Tenant_.id))
                    );
            }
            if (criteria.getMasterCategoriesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMasterCategoriesId(),
                            root -> root.join(Customer_.masterCategories, JoinType.LEFT).get(MasterCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
