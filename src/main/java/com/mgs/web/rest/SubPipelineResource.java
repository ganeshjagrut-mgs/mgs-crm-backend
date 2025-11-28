package com.mgs.web.rest;

import com.mgs.repository.SubPipelineRepository;
import com.mgs.service.SubPipelineQueryService;
import com.mgs.service.SubPipelineService;
import com.mgs.service.criteria.SubPipelineCriteria;
import com.mgs.service.dto.SubPipelineDTO;
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
 * REST controller for managing {@link com.mgs.domain.SubPipeline}.
 */
@RestController
@RequestMapping("/api/sub-pipelines")
public class SubPipelineResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubPipelineResource.class);

    private static final String ENTITY_NAME = "subPipeline";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubPipelineService subPipelineService;

    private final SubPipelineRepository subPipelineRepository;

    private final SubPipelineQueryService subPipelineQueryService;

    public SubPipelineResource(
        SubPipelineService subPipelineService,
        SubPipelineRepository subPipelineRepository,
        SubPipelineQueryService subPipelineQueryService
    ) {
        this.subPipelineService = subPipelineService;
        this.subPipelineRepository = subPipelineRepository;
        this.subPipelineQueryService = subPipelineQueryService;
    }

    /**
     * {@code POST  /sub-pipelines} : Create a new subPipeline.
     *
     * @param subPipelineDTO the subPipelineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subPipelineDTO, or with status {@code 400 (Bad Request)} if the subPipeline has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubPipelineDTO> createSubPipeline(@Valid @RequestBody SubPipelineDTO subPipelineDTO) throws URISyntaxException {
        LOG.debug("REST request to save SubPipeline : {}", subPipelineDTO);
        if (subPipelineDTO.getId() != null) {
            throw new BadRequestAlertException("A new subPipeline cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subPipelineDTO = subPipelineService.save(subPipelineDTO);
        return ResponseEntity.created(new URI("/api/sub-pipelines/" + subPipelineDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, subPipelineDTO.getId().toString()))
            .body(subPipelineDTO);
    }

    /**
     * {@code PUT  /sub-pipelines/:id} : Updates an existing subPipeline.
     *
     * @param id the id of the subPipelineDTO to save.
     * @param subPipelineDTO the subPipelineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subPipelineDTO,
     * or with status {@code 400 (Bad Request)} if the subPipelineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subPipelineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubPipelineDTO> updateSubPipeline(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubPipelineDTO subPipelineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubPipeline : {}, {}", id, subPipelineDTO);
        if (subPipelineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subPipelineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subPipelineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subPipelineDTO = subPipelineService.update(subPipelineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subPipelineDTO.getId().toString()))
            .body(subPipelineDTO);
    }

    /**
     * {@code PATCH  /sub-pipelines/:id} : Partial updates given fields of an existing subPipeline, field will ignore if it is null
     *
     * @param id the id of the subPipelineDTO to save.
     * @param subPipelineDTO the subPipelineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subPipelineDTO,
     * or with status {@code 400 (Bad Request)} if the subPipelineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subPipelineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subPipelineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubPipelineDTO> partialUpdateSubPipeline(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubPipelineDTO subPipelineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubPipeline partially : {}, {}", id, subPipelineDTO);
        if (subPipelineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subPipelineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subPipelineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubPipelineDTO> result = subPipelineService.partialUpdate(subPipelineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subPipelineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-pipelines} : get all the subPipelines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subPipelines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubPipelineDTO>> getAllSubPipelines(
        SubPipelineCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SubPipelines by criteria: {}", criteria);

        Page<SubPipelineDTO> page = subPipelineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-pipelines/count} : count all the subPipelines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubPipelines(SubPipelineCriteria criteria) {
        LOG.debug("REST request to count SubPipelines by criteria: {}", criteria);
        return ResponseEntity.ok().body(subPipelineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-pipelines/:id} : get the "id" subPipeline.
     *
     * @param id the id of the subPipelineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subPipelineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubPipelineDTO> getSubPipeline(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubPipeline : {}", id);
        Optional<SubPipelineDTO> subPipelineDTO = subPipelineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subPipelineDTO);
    }

    /**
     * {@code DELETE  /sub-pipelines/:id} : delete the "id" subPipeline.
     *
     * @param id the id of the subPipelineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubPipeline(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubPipeline : {}", id);
        subPipelineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
