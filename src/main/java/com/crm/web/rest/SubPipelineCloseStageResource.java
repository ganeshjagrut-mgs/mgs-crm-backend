package com.crm.web.rest;

import com.crm.repository.SubPipelineCloseStageRepository;
import com.crm.service.SubPipelineCloseStageQueryService;
import com.crm.service.SubPipelineCloseStageService;
import com.crm.service.criteria.SubPipelineCloseStageCriteria;
import com.crm.service.dto.SubPipelineCloseStageDTO;
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
 * REST controller for managing {@link com.crm.domain.SubPipelineCloseStage}.
 */
@RestController
@RequestMapping("/api/sub-pipeline-close-stages")
public class SubPipelineCloseStageResource {

    private final Logger log = LoggerFactory.getLogger(SubPipelineCloseStageResource.class);

    private static final String ENTITY_NAME = "subPipelineCloseStage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubPipelineCloseStageService subPipelineCloseStageService;

    private final SubPipelineCloseStageRepository subPipelineCloseStageRepository;

    private final SubPipelineCloseStageQueryService subPipelineCloseStageQueryService;

    public SubPipelineCloseStageResource(
        SubPipelineCloseStageService subPipelineCloseStageService,
        SubPipelineCloseStageRepository subPipelineCloseStageRepository,
        SubPipelineCloseStageQueryService subPipelineCloseStageQueryService
    ) {
        this.subPipelineCloseStageService = subPipelineCloseStageService;
        this.subPipelineCloseStageRepository = subPipelineCloseStageRepository;
        this.subPipelineCloseStageQueryService = subPipelineCloseStageQueryService;
    }

    /**
     * {@code POST  /sub-pipeline-close-stages} : Create a new subPipelineCloseStage.
     *
     * @param subPipelineCloseStageDTO the subPipelineCloseStageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subPipelineCloseStageDTO, or with status {@code 400 (Bad Request)} if the subPipelineCloseStage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubPipelineCloseStageDTO> createSubPipelineCloseStage(
        @Valid @RequestBody SubPipelineCloseStageDTO subPipelineCloseStageDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SubPipelineCloseStage : {}", subPipelineCloseStageDTO);
        if (subPipelineCloseStageDTO.getId() != null) {
            throw new BadRequestAlertException("A new subPipelineCloseStage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubPipelineCloseStageDTO result = subPipelineCloseStageService.save(subPipelineCloseStageDTO);
        return ResponseEntity
            .created(new URI("/api/sub-pipeline-close-stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-pipeline-close-stages/:id} : Updates an existing subPipelineCloseStage.
     *
     * @param id the id of the subPipelineCloseStageDTO to save.
     * @param subPipelineCloseStageDTO the subPipelineCloseStageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subPipelineCloseStageDTO,
     * or with status {@code 400 (Bad Request)} if the subPipelineCloseStageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subPipelineCloseStageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubPipelineCloseStageDTO> updateSubPipelineCloseStage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubPipelineCloseStageDTO subPipelineCloseStageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubPipelineCloseStage : {}, {}", id, subPipelineCloseStageDTO);
        if (subPipelineCloseStageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subPipelineCloseStageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subPipelineCloseStageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubPipelineCloseStageDTO result = subPipelineCloseStageService.update(subPipelineCloseStageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subPipelineCloseStageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-pipeline-close-stages/:id} : Partial updates given fields of an existing subPipelineCloseStage, field will ignore if it is null
     *
     * @param id the id of the subPipelineCloseStageDTO to save.
     * @param subPipelineCloseStageDTO the subPipelineCloseStageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subPipelineCloseStageDTO,
     * or with status {@code 400 (Bad Request)} if the subPipelineCloseStageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subPipelineCloseStageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subPipelineCloseStageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubPipelineCloseStageDTO> partialUpdateSubPipelineCloseStage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubPipelineCloseStageDTO subPipelineCloseStageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubPipelineCloseStage partially : {}, {}", id, subPipelineCloseStageDTO);
        if (subPipelineCloseStageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subPipelineCloseStageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subPipelineCloseStageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubPipelineCloseStageDTO> result = subPipelineCloseStageService.partialUpdate(subPipelineCloseStageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subPipelineCloseStageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-pipeline-close-stages} : get all the subPipelineCloseStages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subPipelineCloseStages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubPipelineCloseStageDTO>> getAllSubPipelineCloseStages(
        SubPipelineCloseStageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SubPipelineCloseStages by criteria: {}", criteria);

        Page<SubPipelineCloseStageDTO> page = subPipelineCloseStageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-pipeline-close-stages/count} : count all the subPipelineCloseStages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubPipelineCloseStages(SubPipelineCloseStageCriteria criteria) {
        log.debug("REST request to count SubPipelineCloseStages by criteria: {}", criteria);
        return ResponseEntity.ok().body(subPipelineCloseStageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-pipeline-close-stages/:id} : get the "id" subPipelineCloseStage.
     *
     * @param id the id of the subPipelineCloseStageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subPipelineCloseStageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubPipelineCloseStageDTO> getSubPipelineCloseStage(@PathVariable("id") Long id) {
        log.debug("REST request to get SubPipelineCloseStage : {}", id);
        Optional<SubPipelineCloseStageDTO> subPipelineCloseStageDTO = subPipelineCloseStageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subPipelineCloseStageDTO);
    }

    /**
     * {@code DELETE  /sub-pipeline-close-stages/:id} : delete the "id" subPipelineCloseStage.
     *
     * @param id the id of the subPipelineCloseStageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubPipelineCloseStage(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubPipelineCloseStage : {}", id);
        subPipelineCloseStageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
