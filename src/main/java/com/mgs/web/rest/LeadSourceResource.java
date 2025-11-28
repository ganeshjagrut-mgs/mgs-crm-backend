package com.mgs.web.rest;

import com.mgs.repository.LeadSourceRepository;
import com.mgs.service.LeadSourceQueryService;
import com.mgs.service.LeadSourceService;
import com.mgs.service.criteria.LeadSourceCriteria;
import com.mgs.service.dto.LeadSourceDTO;
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
 * REST controller for managing {@link com.mgs.domain.LeadSource}.
 */
@RestController
@RequestMapping("/api/lead-sources")
public class LeadSourceResource {

    private static final Logger LOG = LoggerFactory.getLogger(LeadSourceResource.class);

    private static final String ENTITY_NAME = "leadSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeadSourceService leadSourceService;

    private final LeadSourceRepository leadSourceRepository;

    private final LeadSourceQueryService leadSourceQueryService;

    public LeadSourceResource(
        LeadSourceService leadSourceService,
        LeadSourceRepository leadSourceRepository,
        LeadSourceQueryService leadSourceQueryService
    ) {
        this.leadSourceService = leadSourceService;
        this.leadSourceRepository = leadSourceRepository;
        this.leadSourceQueryService = leadSourceQueryService;
    }

    /**
     * {@code POST  /lead-sources} : Create a new leadSource.
     *
     * @param leadSourceDTO the leadSourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leadSourceDTO, or with status {@code 400 (Bad Request)} if the leadSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LeadSourceDTO> createLeadSource(@Valid @RequestBody LeadSourceDTO leadSourceDTO) throws URISyntaxException {
        LOG.debug("REST request to save LeadSource : {}", leadSourceDTO);
        if (leadSourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new leadSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        leadSourceDTO = leadSourceService.save(leadSourceDTO);
        return ResponseEntity.created(new URI("/api/lead-sources/" + leadSourceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, leadSourceDTO.getId().toString()))
            .body(leadSourceDTO);
    }

    /**
     * {@code PUT  /lead-sources/:id} : Updates an existing leadSource.
     *
     * @param id the id of the leadSourceDTO to save.
     * @param leadSourceDTO the leadSourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leadSourceDTO,
     * or with status {@code 400 (Bad Request)} if the leadSourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leadSourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LeadSourceDTO> updateLeadSource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeadSourceDTO leadSourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LeadSource : {}, {}", id, leadSourceDTO);
        if (leadSourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leadSourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leadSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        leadSourceDTO = leadSourceService.update(leadSourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leadSourceDTO.getId().toString()))
            .body(leadSourceDTO);
    }

    /**
     * {@code PATCH  /lead-sources/:id} : Partial updates given fields of an existing leadSource, field will ignore if it is null
     *
     * @param id the id of the leadSourceDTO to save.
     * @param leadSourceDTO the leadSourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leadSourceDTO,
     * or with status {@code 400 (Bad Request)} if the leadSourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leadSourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leadSourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeadSourceDTO> partialUpdateLeadSource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeadSourceDTO leadSourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LeadSource partially : {}, {}", id, leadSourceDTO);
        if (leadSourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leadSourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leadSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeadSourceDTO> result = leadSourceService.partialUpdate(leadSourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leadSourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lead-sources} : get all the leadSources.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leadSources in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LeadSourceDTO>> getAllLeadSources(
        LeadSourceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get LeadSources by criteria: {}", criteria);

        Page<LeadSourceDTO> page = leadSourceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lead-sources/count} : count all the leadSources.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLeadSources(LeadSourceCriteria criteria) {
        LOG.debug("REST request to count LeadSources by criteria: {}", criteria);
        return ResponseEntity.ok().body(leadSourceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lead-sources/:id} : get the "id" leadSource.
     *
     * @param id the id of the leadSourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leadSourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeadSourceDTO> getLeadSource(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LeadSource : {}", id);
        Optional<LeadSourceDTO> leadSourceDTO = leadSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leadSourceDTO);
    }

    /**
     * {@code DELETE  /lead-sources/:id} : delete the "id" leadSource.
     *
     * @param id the id of the leadSourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeadSource(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LeadSource : {}", id);
        leadSourceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
