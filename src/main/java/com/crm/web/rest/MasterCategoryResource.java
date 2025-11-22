package com.crm.web.rest;

import com.crm.repository.MasterCategoryRepository;
import com.crm.service.MasterCategoryQueryService;
import com.crm.service.MasterCategoryService;
import com.crm.service.criteria.MasterCategoryCriteria;
import com.crm.service.dto.MasterCategoryDTO;
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
 * REST controller for managing {@link com.crm.domain.MasterCategory}.
 */
@RestController
@RequestMapping("/api/master-categories")
public class MasterCategoryResource {

    private final Logger log = LoggerFactory.getLogger(MasterCategoryResource.class);

    private static final String ENTITY_NAME = "masterCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MasterCategoryService masterCategoryService;

    private final MasterCategoryRepository masterCategoryRepository;

    private final MasterCategoryQueryService masterCategoryQueryService;

    public MasterCategoryResource(
        MasterCategoryService masterCategoryService,
        MasterCategoryRepository masterCategoryRepository,
        MasterCategoryQueryService masterCategoryQueryService
    ) {
        this.masterCategoryService = masterCategoryService;
        this.masterCategoryRepository = masterCategoryRepository;
        this.masterCategoryQueryService = masterCategoryQueryService;
    }

    /**
     * {@code POST  /master-categories} : Create a new masterCategory.
     *
     * @param masterCategoryDTO the masterCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new masterCategoryDTO, or with status {@code 400 (Bad Request)} if the masterCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MasterCategoryDTO> createMasterCategory(@Valid @RequestBody MasterCategoryDTO masterCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save MasterCategory : {}", masterCategoryDTO);
        if (masterCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new masterCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MasterCategoryDTO result = masterCategoryService.save(masterCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/master-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /master-categories/:id} : Updates an existing masterCategory.
     *
     * @param id the id of the masterCategoryDTO to save.
     * @param masterCategoryDTO the masterCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the masterCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the masterCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MasterCategoryDTO> updateMasterCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MasterCategoryDTO masterCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MasterCategory : {}, {}", id, masterCategoryDTO);
        if (masterCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MasterCategoryDTO result = masterCategoryService.update(masterCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /master-categories/:id} : Partial updates given fields of an existing masterCategory, field will ignore if it is null
     *
     * @param id the id of the masterCategoryDTO to save.
     * @param masterCategoryDTO the masterCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the masterCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the masterCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the masterCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MasterCategoryDTO> partialUpdateMasterCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MasterCategoryDTO masterCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MasterCategory partially : {}, {}", id, masterCategoryDTO);
        if (masterCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MasterCategoryDTO> result = masterCategoryService.partialUpdate(masterCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /master-categories} : get all the masterCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of masterCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MasterCategoryDTO>> getAllMasterCategories(
        MasterCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MasterCategories by criteria: {}", criteria);

        Page<MasterCategoryDTO> page = masterCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /master-categories/count} : count all the masterCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMasterCategories(MasterCategoryCriteria criteria) {
        log.debug("REST request to count MasterCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(masterCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /master-categories/:id} : get the "id" masterCategory.
     *
     * @param id the id of the masterCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the masterCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MasterCategoryDTO> getMasterCategory(@PathVariable("id") Long id) {
        log.debug("REST request to get MasterCategory : {}", id);
        Optional<MasterCategoryDTO> masterCategoryDTO = masterCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(masterCategoryDTO);
    }

    /**
     * {@code DELETE  /master-categories/:id} : delete the "id" masterCategory.
     *
     * @param id the id of the masterCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMasterCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete MasterCategory : {}", id);
        masterCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
