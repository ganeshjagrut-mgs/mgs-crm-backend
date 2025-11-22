package com.crm.web.rest;

import com.crm.repository.CustomerCompanyRepository;
import com.crm.service.CustomerCompanyQueryService;
import com.crm.service.CustomerCompanyService;
import com.crm.service.criteria.CustomerCompanyCriteria;
import com.crm.service.dto.CustomerCompanyDTO;
import com.crm.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.crm.domain.CustomerCompany}.
 */
@RestController
@RequestMapping("/api/customer-companies")
public class CustomerCompanyResource {

    private final Logger log = LoggerFactory.getLogger(CustomerCompanyResource.class);

    private static final String ENTITY_NAME = "customerCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerCompanyService customerCompanyService;

    private final CustomerCompanyRepository customerCompanyRepository;

    private final CustomerCompanyQueryService customerCompanyQueryService;

    public CustomerCompanyResource(
        CustomerCompanyService customerCompanyService,
        CustomerCompanyRepository customerCompanyRepository,
        CustomerCompanyQueryService customerCompanyQueryService
    ) {
        this.customerCompanyService = customerCompanyService;
        this.customerCompanyRepository = customerCompanyRepository;
        this.customerCompanyQueryService = customerCompanyQueryService;
    }

    /**
     * {@code POST  /customer-companies} : Create a new customerCompany.
     *
     * @param customerCompanyDTO the customerCompanyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerCompanyDTO, or with status {@code 400 (Bad Request)} if the customerCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerCompanyDTO> createCustomerCompany(@Valid @RequestBody CustomerCompanyDTO customerCompanyDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerCompany : {}", customerCompanyDTO);
        if (customerCompanyDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerCompanyDTO result = customerCompanyService.save(customerCompanyDTO);
        return ResponseEntity
            .created(new URI("/api/customer-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-companies/:id} : Updates an existing customerCompany.
     *
     * @param id the id of the customerCompanyDTO to save.
     * @param customerCompanyDTO the customerCompanyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerCompanyDTO,
     * or with status {@code 400 (Bad Request)} if the customerCompanyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerCompanyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerCompanyDTO> updateCustomerCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerCompanyDTO customerCompanyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerCompany : {}, {}", id, customerCompanyDTO);
        if (customerCompanyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerCompanyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerCompanyDTO result = customerCompanyService.update(customerCompanyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerCompanyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-companies/:id} : Partial updates given fields of an existing customerCompany, field will ignore if it is null
     *
     * @param id the id of the customerCompanyDTO to save.
     * @param customerCompanyDTO the customerCompanyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerCompanyDTO,
     * or with status {@code 400 (Bad Request)} if the customerCompanyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerCompanyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerCompanyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerCompanyDTO> partialUpdateCustomerCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerCompanyDTO customerCompanyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerCompany partially : {}, {}", id, customerCompanyDTO);
        if (customerCompanyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerCompanyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerCompanyDTO> result = customerCompanyService.partialUpdate(customerCompanyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerCompanyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-companies} : get all the customerCompanies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerCompanies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerCompanyDTO>> getAllCustomerCompanies(
        CustomerCompanyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CustomerCompanies by criteria: {}", criteria);

        Page<CustomerCompanyDTO> page = customerCompanyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-companies/count} : count all the customerCompanies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerCompanies(CustomerCompanyCriteria criteria) {
        log.debug("REST request to count CustomerCompanies by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerCompanyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-companies/:id} : get the "id" customerCompany.
     *
     * @param id the id of the customerCompanyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerCompanyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerCompanyDTO> getCustomerCompany(@PathVariable("id") Long id) {
        log.debug("REST request to get CustomerCompany : {}", id);
        Optional<CustomerCompanyDTO> customerCompanyDTO = customerCompanyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerCompanyDTO);
    }

    /**
     * {@code DELETE  /customer-companies/:id} : delete the "id" customerCompany.
     *
     * @param id the id of the customerCompanyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerCompany(@PathVariable("id") Long id) {
        log.debug("REST request to delete CustomerCompany : {}", id);
        customerCompanyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
