package com.mgs.web.rest;

import com.mgs.repository.TenantSubscriptionRepository;
import com.mgs.service.TenantSubscriptionQueryService;
import com.mgs.service.TenantSubscriptionService;
import com.mgs.service.criteria.TenantSubscriptionCriteria;
import com.mgs.service.dto.TenantSubscriptionDTO;
import com.mgs.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mgs.domain.TenantSubscription}.
 */
@RestController
@RequestMapping("/api/tenant-subscriptions")
public class TenantSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TenantSubscriptionResource.class);

    private static final String ENTITY_NAME = "tenantSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantSubscriptionService tenantSubscriptionService;

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    private final TenantSubscriptionQueryService tenantSubscriptionQueryService;

    public TenantSubscriptionResource(
        TenantSubscriptionService tenantSubscriptionService,
        TenantSubscriptionRepository tenantSubscriptionRepository,
        TenantSubscriptionQueryService tenantSubscriptionQueryService
    ) {
        this.tenantSubscriptionService = tenantSubscriptionService;
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.tenantSubscriptionQueryService = tenantSubscriptionQueryService;
    }

    /**
     * {@code POST  /tenant-subscriptions} : Create a new tenantSubscription.
     *
     * @param tenantSubscriptionDTO the tenantSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantSubscriptionDTO, or with status {@code 400 (Bad Request)} if the tenantSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TenantSubscriptionDTO> createTenantSubscription(@Valid @RequestBody TenantSubscriptionDTO tenantSubscriptionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TenantSubscription : {}", tenantSubscriptionDTO);
        if (tenantSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new tenantSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tenantSubscriptionDTO = tenantSubscriptionService.save(tenantSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/tenant-subscriptions/" + tenantSubscriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tenantSubscriptionDTO.getId().toString()))
            .body(tenantSubscriptionDTO);
    }

    /**
     * {@code PUT  /tenant-subscriptions/:id} : Updates an existing tenantSubscription.
     *
     * @param id the id of the tenantSubscriptionDTO to save.
     * @param tenantSubscriptionDTO the tenantSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the tenantSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantSubscriptionDTO> updateTenantSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantSubscriptionDTO tenantSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TenantSubscription : {}, {}", id, tenantSubscriptionDTO);
        if (tenantSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tenantSubscriptionDTO = tenantSubscriptionService.update(tenantSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantSubscriptionDTO.getId().toString()))
            .body(tenantSubscriptionDTO);
    }

    /**
     * {@code PATCH  /tenant-subscriptions/:id} : Partial updates given fields of an existing tenantSubscription, field will ignore if it is null
     *
     * @param id the id of the tenantSubscriptionDTO to save.
     * @param tenantSubscriptionDTO the tenantSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the tenantSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tenantSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TenantSubscriptionDTO> partialUpdateTenantSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantSubscriptionDTO tenantSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TenantSubscription partially : {}, {}", id, tenantSubscriptionDTO);
        if (tenantSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantSubscriptionDTO> result = tenantSubscriptionService.partialUpdate(tenantSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-subscriptions} : get all the tenantSubscriptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TenantSubscriptionDTO>> getAllTenantSubscriptions(
        TenantSubscriptionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TenantSubscriptions by criteria: {}", criteria);

        Page<TenantSubscriptionDTO> page = tenantSubscriptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tenant-subscriptions/count} : count all the tenantSubscriptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTenantSubscriptions(TenantSubscriptionCriteria criteria) {
        LOG.debug("REST request to count TenantSubscriptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(tenantSubscriptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tenant-subscriptions/:id} : get the "id" tenantSubscription.
     *
     * @param id the id of the tenantSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TenantSubscriptionDTO> getTenantSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TenantSubscription : {}", id);
        Optional<TenantSubscriptionDTO> tenantSubscriptionDTO = tenantSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tenantSubscriptionDTO);
    }

    /**
     * {@code DELETE  /tenant-subscriptions/:id} : delete the "id" tenantSubscription.
     *
     * @param id the id of the tenantSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenantSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TenantSubscription : {}", id);
        tenantSubscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
