package com.crm.web.rest;

import com.crm.repository.PipelineTagRepository;
import com.crm.service.PipelineTagQueryService;
import com.crm.service.PipelineTagService;
import com.crm.service.criteria.PipelineTagCriteria;
import com.crm.service.dto.PipelineTagDTO;
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
 * REST controller for managing {@link com.crm.domain.PipelineTag}.
 */
@RestController
@RequestMapping("/api/pipeline-tags")
public class PipelineTagResource {

    private final Logger log = LoggerFactory.getLogger(PipelineTagResource.class);

    private static final String ENTITY_NAME = "pipelineTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PipelineTagService pipelineTagService;

    private final PipelineTagRepository pipelineTagRepository;

    private final PipelineTagQueryService pipelineTagQueryService;

    public PipelineTagResource(
        PipelineTagService pipelineTagService,
        PipelineTagRepository pipelineTagRepository,
        PipelineTagQueryService pipelineTagQueryService
    ) {
        this.pipelineTagService = pipelineTagService;
        this.pipelineTagRepository = pipelineTagRepository;
        this.pipelineTagQueryService = pipelineTagQueryService;
    }

    /**
     * {@code POST  /pipeline-tags} : Create a new pipelineTag.
     *
     * @param pipelineTagDTO the pipelineTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pipelineTagDTO, or with status {@code 400 (Bad Request)} if the pipelineTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PipelineTagDTO> createPipelineTag(@Valid @RequestBody PipelineTagDTO pipelineTagDTO) throws URISyntaxException {
        log.debug("REST request to save PipelineTag : {}", pipelineTagDTO);
        if (pipelineTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new pipelineTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PipelineTagDTO result = pipelineTagService.save(pipelineTagDTO);
        return ResponseEntity
            .created(new URI("/api/pipeline-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pipeline-tags/:id} : Updates an existing pipelineTag.
     *
     * @param id the id of the pipelineTagDTO to save.
     * @param pipelineTagDTO the pipelineTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pipelineTagDTO,
     * or with status {@code 400 (Bad Request)} if the pipelineTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pipelineTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PipelineTagDTO> updatePipelineTag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PipelineTagDTO pipelineTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PipelineTag : {}, {}", id, pipelineTagDTO);
        if (pipelineTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pipelineTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pipelineTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PipelineTagDTO result = pipelineTagService.update(pipelineTagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pipelineTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pipeline-tags/:id} : Partial updates given fields of an existing pipelineTag, field will ignore if it is null
     *
     * @param id the id of the pipelineTagDTO to save.
     * @param pipelineTagDTO the pipelineTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pipelineTagDTO,
     * or with status {@code 400 (Bad Request)} if the pipelineTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pipelineTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pipelineTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PipelineTagDTO> partialUpdatePipelineTag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PipelineTagDTO pipelineTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PipelineTag partially : {}, {}", id, pipelineTagDTO);
        if (pipelineTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pipelineTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pipelineTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PipelineTagDTO> result = pipelineTagService.partialUpdate(pipelineTagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pipelineTagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pipeline-tags} : get all the pipelineTags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pipelineTags in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PipelineTagDTO>> getAllPipelineTags(
        PipelineTagCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PipelineTags by criteria: {}", criteria);

        Page<PipelineTagDTO> page = pipelineTagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pipeline-tags/count} : count all the pipelineTags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPipelineTags(PipelineTagCriteria criteria) {
        log.debug("REST request to count PipelineTags by criteria: {}", criteria);
        return ResponseEntity.ok().body(pipelineTagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pipeline-tags/:id} : get the "id" pipelineTag.
     *
     * @param id the id of the pipelineTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pipelineTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PipelineTagDTO> getPipelineTag(@PathVariable("id") Long id) {
        log.debug("REST request to get PipelineTag : {}", id);
        Optional<PipelineTagDTO> pipelineTagDTO = pipelineTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pipelineTagDTO);
    }

    /**
     * {@code DELETE  /pipeline-tags/:id} : delete the "id" pipelineTag.
     *
     * @param id the id of the pipelineTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipelineTag(@PathVariable("id") Long id) {
        log.debug("REST request to delete PipelineTag : {}", id);
        pipelineTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
