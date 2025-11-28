package com.mgs.web.rest;

import com.mgs.repository.TenantBrandingRepository;
import com.mgs.service.TenantBrandingQueryService;
import com.mgs.service.TenantBrandingService;
import com.mgs.service.criteria.TenantBrandingCriteria;
import com.mgs.service.dto.TenantBrandingDTO;
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
 * REST controller for managing {@link com.mgs.domain.TenantBranding}.
 */
@RestController
@RequestMapping("/api/tenant-brandings")
public class TenantBrandingResource {

    private static final Logger LOG = LoggerFactory.getLogger(TenantBrandingResource.class);

    private static final String ENTITY_NAME = "tenantBranding";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantBrandingService tenantBrandingService;

    private final TenantBrandingRepository tenantBrandingRepository;

    private final TenantBrandingQueryService tenantBrandingQueryService;

    public TenantBrandingResource(
        TenantBrandingService tenantBrandingService,
        TenantBrandingRepository tenantBrandingRepository,
        TenantBrandingQueryService tenantBrandingQueryService
    ) {
        this.tenantBrandingService = tenantBrandingService;
        this.tenantBrandingRepository = tenantBrandingRepository;
        this.tenantBrandingQueryService = tenantBrandingQueryService;
    }

    /**
     * {@code POST  /tenant-brandings} : Create a new tenantBranding.
     *
     * @param tenantBrandingDTO the tenantBrandingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantBrandingDTO, or with status {@code 400 (Bad Request)} if the tenantBranding has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TenantBrandingDTO> createTenantBranding(@Valid @RequestBody TenantBrandingDTO tenantBrandingDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TenantBranding : {}", tenantBrandingDTO);
        if (tenantBrandingDTO.getId() != null) {
            throw new BadRequestAlertException("A new tenantBranding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tenantBrandingDTO = tenantBrandingService.save(tenantBrandingDTO);
        return ResponseEntity.created(new URI("/api/tenant-brandings/" + tenantBrandingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tenantBrandingDTO.getId().toString()))
            .body(tenantBrandingDTO);
    }

    /**
     * {@code PUT  /tenant-brandings/:id} : Updates an existing tenantBranding.
     *
     * @param id the id of the tenantBrandingDTO to save.
     * @param tenantBrandingDTO the tenantBrandingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantBrandingDTO,
     * or with status {@code 400 (Bad Request)} if the tenantBrandingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantBrandingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantBrandingDTO> updateTenantBranding(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantBrandingDTO tenantBrandingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TenantBranding : {}, {}", id, tenantBrandingDTO);
        if (tenantBrandingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantBrandingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantBrandingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tenantBrandingDTO = tenantBrandingService.update(tenantBrandingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantBrandingDTO.getId().toString()))
            .body(tenantBrandingDTO);
    }

    /**
     * {@code PATCH  /tenant-brandings/:id} : Partial updates given fields of an existing tenantBranding, field will ignore if it is null
     *
     * @param id the id of the tenantBrandingDTO to save.
     * @param tenantBrandingDTO the tenantBrandingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantBrandingDTO,
     * or with status {@code 400 (Bad Request)} if the tenantBrandingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tenantBrandingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantBrandingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TenantBrandingDTO> partialUpdateTenantBranding(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantBrandingDTO tenantBrandingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TenantBranding partially : {}, {}", id, tenantBrandingDTO);
        if (tenantBrandingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantBrandingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantBrandingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantBrandingDTO> result = tenantBrandingService.partialUpdate(tenantBrandingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantBrandingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-brandings} : get all the tenantBrandings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantBrandings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TenantBrandingDTO>> getAllTenantBrandings(
        TenantBrandingCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TenantBrandings by criteria: {}", criteria);

        Page<TenantBrandingDTO> page = tenantBrandingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tenant-brandings/count} : count all the tenantBrandings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTenantBrandings(TenantBrandingCriteria criteria) {
        LOG.debug("REST request to count TenantBrandings by criteria: {}", criteria);
        return ResponseEntity.ok().body(tenantBrandingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tenant-brandings/:id} : get the "id" tenantBranding.
     *
     * @param id the id of the tenantBrandingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantBrandingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TenantBrandingDTO> getTenantBranding(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TenantBranding : {}", id);
        Optional<TenantBrandingDTO> tenantBrandingDTO = tenantBrandingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tenantBrandingDTO);
    }

    /**
     * {@code DELETE  /tenant-brandings/:id} : delete the "id" tenantBranding.
     *
     * @param id the id of the tenantBrandingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenantBranding(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TenantBranding : {}", id);
        tenantBrandingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
