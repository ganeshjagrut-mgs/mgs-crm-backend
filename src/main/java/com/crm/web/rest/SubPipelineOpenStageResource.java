package com.crm.web.rest;

import com.crm.repository.SubPipelineOpenStageRepository;
import com.crm.service.SubPipelineOpenStageQueryService;
import com.crm.service.SubPipelineOpenStageService;
import com.crm.service.criteria.SubPipelineOpenStageCriteria;
import com.crm.service.dto.SubPipelineOpenStageDTO;
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
 * REST controller for managing {@link com.crm.domain.SubPipelineOpenStage}.
 */
@RestController
@RequestMapping("/api/sub-pipeline-open-stages")
public class SubPipelineOpenStageResource {

    private final Logger log = LoggerFactory.getLogger(SubPipelineOpenStageResource.class);

    private static final String ENTITY_NAME = "subPipelineOpenStage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubPipelineOpenStageService subPipelineOpenStageService;

    private final SubPipelineOpenStageRepository subPipelineOpenStageRepository;

    private final SubPipelineOpenStageQueryService subPipelineOpenStageQueryService;

    public SubPipelineOpenStageResource(
        SubPipelineOpenStageService subPipelineOpenStageService,
        SubPipelineOpenStageRepository subPipelineOpenStageRepository,
        SubPipelineOpenStageQueryService subPipelineOpenStageQueryService
    ) {
        this.subPipelineOpenStageService = subPipelineOpenStageService;
        this.subPipelineOpenStageRepository = subPipelineOpenStageRepository;
        this.subPipelineOpenStageQueryService = subPipelineOpenStageQueryService;
    }

    /**
     * {@code POST  /sub-pipeline-open-stages} : Create a new subPipelineOpenStage.
     *
     * @param subPipelineOpenStageDTO the subPipelineOpenStageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subPipelineOpenStageDTO, or with status {@code 400 (Bad Request)} if the subPipelineOpenStage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubPipelineOpenStageDTO> createSubPipelineOpenStage(
        @Valid @RequestBody SubPipelineOpenStageDTO subPipelineOpenStageDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SubPipelineOpenStage : {}", subPipelineOpenStageDTO);
        if (subPipelineOpenStageDTO.getId() != null) {
            throw new BadRequestAlertException("A new subPipelineOpenStage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubPipelineOpenStageDTO result = subPipelineOpenStageService.save(subPipelineOpenStageDTO);
        return ResponseEntity
            .created(new URI("/api/sub-pipeline-open-stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-pipeline-open-stages/:id} : Updates an existing subPipelineOpenStage.
     *
     * @param id the id of the subPipelineOpenStageDTO to save.
     * @param subPipelineOpenStageDTO the subPipelineOpenStageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subPipelineOpenStageDTO,
     * or with status {@code 400 (Bad Request)} if the subPipelineOpenStageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subPipelineOpenStageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubPipelineOpenStageDTO> updateSubPipelineOpenStage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubPipelineOpenStageDTO subPipelineOpenStageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubPipelineOpenStage : {}, {}", id, subPipelineOpenStageDTO);
        if (subPipelineOpenStageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subPipelineOpenStageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subPipelineOpenStageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubPipelineOpenStageDTO result = subPipelineOpenStageService.update(subPipelineOpenStageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subPipelineOpenStageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-pipeline-open-stages/:id} : Partial updates given fields of an existing subPipelineOpenStage, field will ignore if it is null
     *
     * @param id the id of the subPipelineOpenStageDTO to save.
     * @param subPipelineOpenStageDTO the subPipelineOpenStageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subPipelineOpenStageDTO,
     * or with status {@code 400 (Bad Request)} if the subPipelineOpenStageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subPipelineOpenStageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subPipelineOpenStageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubPipelineOpenStageDTO> partialUpdateSubPipelineOpenStage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubPipelineOpenStageDTO subPipelineOpenStageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubPipelineOpenStage partially : {}, {}", id, subPipelineOpenStageDTO);
        if (subPipelineOpenStageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subPipelineOpenStageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subPipelineOpenStageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubPipelineOpenStageDTO> result = subPipelineOpenStageService.partialUpdate(subPipelineOpenStageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subPipelineOpenStageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-pipeline-open-stages} : get all the subPipelineOpenStages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subPipelineOpenStages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubPipelineOpenStageDTO>> getAllSubPipelineOpenStages(
        SubPipelineOpenStageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SubPipelineOpenStages by criteria: {}", criteria);

        Page<SubPipelineOpenStageDTO> page = subPipelineOpenStageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-pipeline-open-stages/count} : count all the subPipelineOpenStages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubPipelineOpenStages(SubPipelineOpenStageCriteria criteria) {
        log.debug("REST request to count SubPipelineOpenStages by criteria: {}", criteria);
        return ResponseEntity.ok().body(subPipelineOpenStageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-pipeline-open-stages/:id} : get the "id" subPipelineOpenStage.
     *
     * @param id the id of the subPipelineOpenStageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subPipelineOpenStageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubPipelineOpenStageDTO> getSubPipelineOpenStage(@PathVariable("id") Long id) {
        log.debug("REST request to get SubPipelineOpenStage : {}", id);
        Optional<SubPipelineOpenStageDTO> subPipelineOpenStageDTO = subPipelineOpenStageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subPipelineOpenStageDTO);
    }

    /**
     * {@code DELETE  /sub-pipeline-open-stages/:id} : delete the "id" subPipelineOpenStage.
     *
     * @param id the id of the subPipelineOpenStageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubPipelineOpenStage(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubPipelineOpenStage : {}", id);
        subPipelineOpenStageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
