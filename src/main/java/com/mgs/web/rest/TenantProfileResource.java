package com.mgs.web.rest;

import com.mgs.repository.TenantProfileRepository;
import com.mgs.service.TenantProfileQueryService;
import com.mgs.service.TenantProfileService;
import com.mgs.service.criteria.TenantProfileCriteria;
import com.mgs.service.dto.TenantProfileDTO;
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
 * REST controller for managing {@link com.mgs.domain.TenantProfile}.
 */
@RestController
@RequestMapping("/api/tenant-profiles")
public class TenantProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(TenantProfileResource.class);

    private static final String ENTITY_NAME = "tenantProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantProfileService tenantProfileService;

    private final TenantProfileRepository tenantProfileRepository;

    private final TenantProfileQueryService tenantProfileQueryService;

    public TenantProfileResource(
        TenantProfileService tenantProfileService,
        TenantProfileRepository tenantProfileRepository,
        TenantProfileQueryService tenantProfileQueryService
    ) {
        this.tenantProfileService = tenantProfileService;
        this.tenantProfileRepository = tenantProfileRepository;
        this.tenantProfileQueryService = tenantProfileQueryService;
    }

    /**
     * {@code POST  /tenant-profiles} : Create a new tenantProfile.
     *
     * @param tenantProfileDTO the tenantProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantProfileDTO, or with status {@code 400 (Bad Request)} if the tenantProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TenantProfileDTO> createTenantProfile(@Valid @RequestBody TenantProfileDTO tenantProfileDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TenantProfile : {}", tenantProfileDTO);
        if (tenantProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new tenantProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tenantProfileDTO = tenantProfileService.save(tenantProfileDTO);
        return ResponseEntity.created(new URI("/api/tenant-profiles/" + tenantProfileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tenantProfileDTO.getId().toString()))
            .body(tenantProfileDTO);
    }

    /**
     * {@code PUT  /tenant-profiles/:id} : Updates an existing tenantProfile.
     *
     * @param id the id of the tenantProfileDTO to save.
     * @param tenantProfileDTO the tenantProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantProfileDTO,
     * or with status {@code 400 (Bad Request)} if the tenantProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantProfileDTO> updateTenantProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantProfileDTO tenantProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TenantProfile : {}, {}", id, tenantProfileDTO);
        if (tenantProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tenantProfileDTO = tenantProfileService.update(tenantProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantProfileDTO.getId().toString()))
            .body(tenantProfileDTO);
    }

    /**
     * {@code PATCH  /tenant-profiles/:id} : Partial updates given fields of an existing tenantProfile, field will ignore if it is null
     *
     * @param id the id of the tenantProfileDTO to save.
     * @param tenantProfileDTO the tenantProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantProfileDTO,
     * or with status {@code 400 (Bad Request)} if the tenantProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tenantProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TenantProfileDTO> partialUpdateTenantProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantProfileDTO tenantProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TenantProfile partially : {}, {}", id, tenantProfileDTO);
        if (tenantProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantProfileDTO> result = tenantProfileService.partialUpdate(tenantProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-profiles} : get all the tenantProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantProfiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TenantProfileDTO>> getAllTenantProfiles(
        TenantProfileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TenantProfiles by criteria: {}", criteria);

        Page<TenantProfileDTO> page = tenantProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tenant-profiles/count} : count all the tenantProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTenantProfiles(TenantProfileCriteria criteria) {
        LOG.debug("REST request to count TenantProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(tenantProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tenant-profiles/:id} : get the "id" tenantProfile.
     *
     * @param id the id of the tenantProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TenantProfileDTO> getTenantProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TenantProfile : {}", id);
        Optional<TenantProfileDTO> tenantProfileDTO = tenantProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tenantProfileDTO);
    }

    /**
     * {@code DELETE  /tenant-profiles/:id} : delete the "id" tenantProfile.
     *
     * @param id the id of the tenantProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenantProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TenantProfile : {}", id);
        tenantProfileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
