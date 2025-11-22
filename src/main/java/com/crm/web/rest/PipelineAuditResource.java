package com.crm.web.rest;

import com.crm.repository.PipelineAuditRepository;
import com.crm.service.PipelineAuditQueryService;
import com.crm.service.PipelineAuditService;
import com.crm.service.criteria.PipelineAuditCriteria;
import com.crm.service.dto.PipelineAuditDTO;
import com.crm.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.crm.domain.PipelineAudit}.
 */
@RestController
@RequestMapping("/api/pipeline-audits")
public class PipelineAuditResource {

    private final Logger log = LoggerFactory.getLogger(PipelineAuditResource.class);

    private static final String ENTITY_NAME = "pipelineAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PipelineAuditService pipelineAuditService;

    private final PipelineAuditRepository pipelineAuditRepository;

    private final PipelineAuditQueryService pipelineAuditQueryService;

    public PipelineAuditResource(
        PipelineAuditService pipelineAuditService,
        PipelineAuditRepository pipelineAuditRepository,
        PipelineAuditQueryService pipelineAuditQueryService
    ) {
        this.pipelineAuditService = pipelineAuditService;
        this.pipelineAuditRepository = pipelineAuditRepository;
        this.pipelineAuditQueryService = pipelineAuditQueryService;
    }

    /**
     * {@code POST  /pipeline-audits} : Create a new pipelineAudit.
     *
     * @param pipelineAuditDTO the pipelineAuditDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pipelineAuditDTO, or with status {@code 400 (Bad Request)} if the pipelineAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PipelineAuditDTO> createPipelineAudit(@RequestBody PipelineAuditDTO pipelineAuditDTO) throws URISyntaxException {
        log.debug("REST request to save PipelineAudit : {}", pipelineAuditDTO);
        if (pipelineAuditDTO.getId() != null) {
            throw new BadRequestAlertException("A new pipelineAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PipelineAuditDTO result = pipelineAuditService.save(pipelineAuditDTO);
        return ResponseEntity
            .created(new URI("/api/pipeline-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pipeline-audits/:id} : Updates an existing pipelineAudit.
     *
     * @param id the id of the pipelineAuditDTO to save.
     * @param pipelineAuditDTO the pipelineAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pipelineAuditDTO,
     * or with status {@code 400 (Bad Request)} if the pipelineAuditDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pipelineAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PipelineAuditDTO> updatePipelineAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PipelineAuditDTO pipelineAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PipelineAudit : {}, {}", id, pipelineAuditDTO);
        if (pipelineAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pipelineAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pipelineAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PipelineAuditDTO result = pipelineAuditService.update(pipelineAuditDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pipelineAuditDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pipeline-audits/:id} : Partial updates given fields of an existing pipelineAudit, field will ignore if it is null
     *
     * @param id the id of the pipelineAuditDTO to save.
     * @param pipelineAuditDTO the pipelineAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pipelineAuditDTO,
     * or with status {@code 400 (Bad Request)} if the pipelineAuditDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pipelineAuditDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pipelineAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PipelineAuditDTO> partialUpdatePipelineAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PipelineAuditDTO pipelineAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PipelineAudit partially : {}, {}", id, pipelineAuditDTO);
        if (pipelineAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pipelineAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pipelineAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PipelineAuditDTO> result = pipelineAuditService.partialUpdate(pipelineAuditDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pipelineAuditDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pipeline-audits} : get all the pipelineAudits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pipelineAudits in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PipelineAuditDTO>> getAllPipelineAudits(
        PipelineAuditCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PipelineAudits by criteria: {}", criteria);

        Page<PipelineAuditDTO> page = pipelineAuditQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pipeline-audits/count} : count all the pipelineAudits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPipelineAudits(PipelineAuditCriteria criteria) {
        log.debug("REST request to count PipelineAudits by criteria: {}", criteria);
        return ResponseEntity.ok().body(pipelineAuditQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pipeline-audits/:id} : get the "id" pipelineAudit.
     *
     * @param id the id of the pipelineAuditDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pipelineAuditDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PipelineAuditDTO> getPipelineAudit(@PathVariable("id") Long id) {
        log.debug("REST request to get PipelineAudit : {}", id);
        Optional<PipelineAuditDTO> pipelineAuditDTO = pipelineAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pipelineAuditDTO);
    }

    /**
     * {@code DELETE  /pipeline-audits/:id} : delete the "id" pipelineAudit.
     *
     * @param id the id of the pipelineAuditDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipelineAudit(@PathVariable("id") Long id) {
        log.debug("REST request to delete PipelineAudit : {}", id);
        pipelineAuditService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
