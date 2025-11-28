package com.mgs.web.rest;

import com.mgs.repository.ComplaintCategoryRepository;
import com.mgs.service.ComplaintCategoryQueryService;
import com.mgs.service.ComplaintCategoryService;
import com.mgs.service.criteria.ComplaintCategoryCriteria;
import com.mgs.service.dto.ComplaintCategoryDTO;
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
 * REST controller for managing {@link com.mgs.domain.ComplaintCategory}.
 */
@RestController
@RequestMapping("/api/complaint-categories")
public class ComplaintCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintCategoryResource.class);

    private static final String ENTITY_NAME = "complaintCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComplaintCategoryService complaintCategoryService;

    private final ComplaintCategoryRepository complaintCategoryRepository;

    private final ComplaintCategoryQueryService complaintCategoryQueryService;

    public ComplaintCategoryResource(
        ComplaintCategoryService complaintCategoryService,
        ComplaintCategoryRepository complaintCategoryRepository,
        ComplaintCategoryQueryService complaintCategoryQueryService
    ) {
        this.complaintCategoryService = complaintCategoryService;
        this.complaintCategoryRepository = complaintCategoryRepository;
        this.complaintCategoryQueryService = complaintCategoryQueryService;
    }

    /**
     * {@code POST  /complaint-categories} : Create a new complaintCategory.
     *
     * @param complaintCategoryDTO the complaintCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new complaintCategoryDTO, or with status {@code 400 (Bad Request)} if the complaintCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ComplaintCategoryDTO> createComplaintCategory(@Valid @RequestBody ComplaintCategoryDTO complaintCategoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ComplaintCategory : {}", complaintCategoryDTO);
        if (complaintCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new complaintCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        complaintCategoryDTO = complaintCategoryService.save(complaintCategoryDTO);
        return ResponseEntity.created(new URI("/api/complaint-categories/" + complaintCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, complaintCategoryDTO.getId().toString()))
            .body(complaintCategoryDTO);
    }

    /**
     * {@code PUT  /complaint-categories/:id} : Updates an existing complaintCategory.
     *
     * @param id the id of the complaintCategoryDTO to save.
     * @param complaintCategoryDTO the complaintCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complaintCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the complaintCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the complaintCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComplaintCategoryDTO> updateComplaintCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComplaintCategoryDTO complaintCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ComplaintCategory : {}, {}", id, complaintCategoryDTO);
        if (complaintCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, complaintCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complaintCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        complaintCategoryDTO = complaintCategoryService.update(complaintCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, complaintCategoryDTO.getId().toString()))
            .body(complaintCategoryDTO);
    }

    /**
     * {@code PATCH  /complaint-categories/:id} : Partial updates given fields of an existing complaintCategory, field will ignore if it is null
     *
     * @param id the id of the complaintCategoryDTO to save.
     * @param complaintCategoryDTO the complaintCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complaintCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the complaintCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the complaintCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the complaintCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComplaintCategoryDTO> partialUpdateComplaintCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComplaintCategoryDTO complaintCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ComplaintCategory partially : {}, {}", id, complaintCategoryDTO);
        if (complaintCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, complaintCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complaintCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComplaintCategoryDTO> result = complaintCategoryService.partialUpdate(complaintCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, complaintCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /complaint-categories} : get all the complaintCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of complaintCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ComplaintCategoryDTO>> getAllComplaintCategories(
        ComplaintCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ComplaintCategories by criteria: {}", criteria);

        Page<ComplaintCategoryDTO> page = complaintCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /complaint-categories/count} : count all the complaintCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countComplaintCategories(ComplaintCategoryCriteria criteria) {
        LOG.debug("REST request to count ComplaintCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(complaintCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /complaint-categories/:id} : get the "id" complaintCategory.
     *
     * @param id the id of the complaintCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the complaintCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintCategoryDTO> getComplaintCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ComplaintCategory : {}", id);
        Optional<ComplaintCategoryDTO> complaintCategoryDTO = complaintCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(complaintCategoryDTO);
    }

    /**
     * {@code DELETE  /complaint-categories/:id} : delete the "id" complaintCategory.
     *
     * @param id the id of the complaintCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaintCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ComplaintCategory : {}", id);
        complaintCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
