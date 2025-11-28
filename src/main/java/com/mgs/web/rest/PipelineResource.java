package com.mgs.web.rest;

import com.mgs.repository.PipelineRepository;
import com.mgs.service.PipelineQueryService;
import com.mgs.service.PipelineService;
import com.mgs.service.criteria.PipelineCriteria;
import com.mgs.service.dto.PipelineDTO;
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
 * REST controller for managing {@link com.mgs.domain.Pipeline}.
 */
@RestController
@RequestMapping("/api/pipelines")
public class PipelineResource {

    private static final Logger LOG = LoggerFactory.getLogger(PipelineResource.class);

    private static final String ENTITY_NAME = "pipeline";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PipelineService pipelineService;

    private final PipelineRepository pipelineRepository;

    private final PipelineQueryService pipelineQueryService;

    public PipelineResource(
        PipelineService pipelineService,
        PipelineRepository pipelineRepository,
        PipelineQueryService pipelineQueryService
    ) {
        this.pipelineService = pipelineService;
        this.pipelineRepository = pipelineRepository;
        this.pipelineQueryService = pipelineQueryService;
    }

    /**
     * {@code POST  /pipelines} : Create a new pipeline.
     *
     * @param pipelineDTO the pipelineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pipelineDTO, or with status {@code 400 (Bad Request)} if the pipeline has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PipelineDTO> createPipeline(@Valid @RequestBody PipelineDTO pipelineDTO) throws URISyntaxException {
        LOG.debug("REST request to save Pipeline : {}", pipelineDTO);
        if (pipelineDTO.getId() != null) {
            throw new BadRequestAlertException("A new pipeline cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pipelineDTO = pipelineService.save(pipelineDTO);
        return ResponseEntity.created(new URI("/api/pipelines/" + pipelineDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, pipelineDTO.getId().toString()))
            .body(pipelineDTO);
    }

    /**
     * {@code PUT  /pipelines/:id} : Updates an existing pipeline.
     *
     * @param id the id of the pipelineDTO to save.
     * @param pipelineDTO the pipelineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pipelineDTO,
     * or with status {@code 400 (Bad Request)} if the pipelineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pipelineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PipelineDTO> updatePipeline(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PipelineDTO pipelineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Pipeline : {}, {}", id, pipelineDTO);
        if (pipelineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pipelineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pipelineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pipelineDTO = pipelineService.update(pipelineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pipelineDTO.getId().toString()))
            .body(pipelineDTO);
    }

    /**
     * {@code PATCH  /pipelines/:id} : Partial updates given fields of an existing pipeline, field will ignore if it is null
     *
     * @param id the id of the pipelineDTO to save.
     * @param pipelineDTO the pipelineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pipelineDTO,
     * or with status {@code 400 (Bad Request)} if the pipelineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pipelineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pipelineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PipelineDTO> partialUpdatePipeline(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PipelineDTO pipelineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Pipeline partially : {}, {}", id, pipelineDTO);
        if (pipelineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pipelineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pipelineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PipelineDTO> result = pipelineService.partialUpdate(pipelineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pipelineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pipelines} : get all the pipelines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pipelines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PipelineDTO>> getAllPipelines(
        PipelineCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Pipelines by criteria: {}", criteria);

        Page<PipelineDTO> page = pipelineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pipelines/count} : count all the pipelines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPipelines(PipelineCriteria criteria) {
        LOG.debug("REST request to count Pipelines by criteria: {}", criteria);
        return ResponseEntity.ok().body(pipelineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pipelines/:id} : get the "id" pipeline.
     *
     * @param id the id of the pipelineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pipelineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PipelineDTO> getPipeline(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pipeline : {}", id);
        Optional<PipelineDTO> pipelineDTO = pipelineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pipelineDTO);
    }

    /**
     * {@code DELETE  /pipelines/:id} : delete the "id" pipeline.
     *
     * @param id the id of the pipelineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pipeline : {}", id);
        pipelineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
