package com.mgs.web.rest;

import com.mgs.repository.ReportRunRepository;
import com.mgs.service.ReportRunQueryService;
import com.mgs.service.ReportRunService;
import com.mgs.service.criteria.ReportRunCriteria;
import com.mgs.service.dto.ReportRunDTO;
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
 * REST controller for managing {@link com.mgs.domain.ReportRun}.
 */
@RestController
@RequestMapping("/api/report-runs")
public class ReportRunResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportRunResource.class);

    private static final String ENTITY_NAME = "reportRun";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportRunService reportRunService;

    private final ReportRunRepository reportRunRepository;

    private final ReportRunQueryService reportRunQueryService;

    public ReportRunResource(
        ReportRunService reportRunService,
        ReportRunRepository reportRunRepository,
        ReportRunQueryService reportRunQueryService
    ) {
        this.reportRunService = reportRunService;
        this.reportRunRepository = reportRunRepository;
        this.reportRunQueryService = reportRunQueryService;
    }

    /**
     * {@code POST  /report-runs} : Create a new reportRun.
     *
     * @param reportRunDTO the reportRunDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportRunDTO, or with status {@code 400 (Bad Request)} if the reportRun has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportRunDTO> createReportRun(@Valid @RequestBody ReportRunDTO reportRunDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReportRun : {}", reportRunDTO);
        if (reportRunDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportRun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportRunDTO = reportRunService.save(reportRunDTO);
        return ResponseEntity.created(new URI("/api/report-runs/" + reportRunDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, reportRunDTO.getId().toString()))
            .body(reportRunDTO);
    }

    /**
     * {@code PUT  /report-runs/:id} : Updates an existing reportRun.
     *
     * @param id the id of the reportRunDTO to save.
     * @param reportRunDTO the reportRunDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportRunDTO,
     * or with status {@code 400 (Bad Request)} if the reportRunDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportRunDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportRunDTO> updateReportRun(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportRunDTO reportRunDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportRun : {}, {}", id, reportRunDTO);
        if (reportRunDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportRunDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportRunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportRunDTO = reportRunService.update(reportRunDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reportRunDTO.getId().toString()))
            .body(reportRunDTO);
    }

    /**
     * {@code PATCH  /report-runs/:id} : Partial updates given fields of an existing reportRun, field will ignore if it is null
     *
     * @param id the id of the reportRunDTO to save.
     * @param reportRunDTO the reportRunDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportRunDTO,
     * or with status {@code 400 (Bad Request)} if the reportRunDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportRunDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportRunDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportRunDTO> partialUpdateReportRun(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportRunDTO reportRunDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportRun partially : {}, {}", id, reportRunDTO);
        if (reportRunDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportRunDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportRunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportRunDTO> result = reportRunService.partialUpdate(reportRunDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reportRunDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-runs} : get all the reportRuns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportRuns in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportRunDTO>> getAllReportRuns(
        ReportRunCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportRuns by criteria: {}", criteria);

        Page<ReportRunDTO> page = reportRunQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-runs/count} : count all the reportRuns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportRuns(ReportRunCriteria criteria) {
        LOG.debug("REST request to count ReportRuns by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportRunQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-runs/:id} : get the "id" reportRun.
     *
     * @param id the id of the reportRunDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportRunDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportRunDTO> getReportRun(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportRun : {}", id);
        Optional<ReportRunDTO> reportRunDTO = reportRunService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportRunDTO);
    }

    /**
     * {@code DELETE  /report-runs/:id} : delete the "id" reportRun.
     *
     * @param id the id of the reportRunDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportRun(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportRun : {}", id);
        reportRunService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
