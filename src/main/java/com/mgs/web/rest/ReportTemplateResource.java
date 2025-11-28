package com.mgs.web.rest;

import com.mgs.repository.ReportTemplateRepository;
import com.mgs.service.ReportTemplateQueryService;
import com.mgs.service.ReportTemplateService;
import com.mgs.service.criteria.ReportTemplateCriteria;
import com.mgs.service.dto.ReportTemplateDTO;
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
 * REST controller for managing {@link com.mgs.domain.ReportTemplate}.
 */
@RestController
@RequestMapping("/api/report-templates")
public class ReportTemplateResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportTemplateResource.class);

    private static final String ENTITY_NAME = "reportTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportTemplateService reportTemplateService;

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateQueryService reportTemplateQueryService;

    public ReportTemplateResource(
        ReportTemplateService reportTemplateService,
        ReportTemplateRepository reportTemplateRepository,
        ReportTemplateQueryService reportTemplateQueryService
    ) {
        this.reportTemplateService = reportTemplateService;
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateQueryService = reportTemplateQueryService;
    }

    /**
     * {@code POST  /report-templates} : Create a new reportTemplate.
     *
     * @param reportTemplateDTO the reportTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportTemplateDTO, or with status {@code 400 (Bad Request)} if the reportTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportTemplateDTO> createReportTemplate(@Valid @RequestBody ReportTemplateDTO reportTemplateDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReportTemplate : {}", reportTemplateDTO);
        if (reportTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportTemplateDTO = reportTemplateService.save(reportTemplateDTO);
        return ResponseEntity.created(new URI("/api/report-templates/" + reportTemplateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, reportTemplateDTO.getId().toString()))
            .body(reportTemplateDTO);
    }

    /**
     * {@code PUT  /report-templates/:id} : Updates an existing reportTemplate.
     *
     * @param id the id of the reportTemplateDTO to save.
     * @param reportTemplateDTO the reportTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the reportTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportTemplateDTO> updateReportTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportTemplateDTO reportTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportTemplate : {}, {}", id, reportTemplateDTO);
        if (reportTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportTemplateDTO = reportTemplateService.update(reportTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reportTemplateDTO.getId().toString()))
            .body(reportTemplateDTO);
    }

    /**
     * {@code PATCH  /report-templates/:id} : Partial updates given fields of an existing reportTemplate, field will ignore if it is null
     *
     * @param id the id of the reportTemplateDTO to save.
     * @param reportTemplateDTO the reportTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the reportTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportTemplateDTO> partialUpdateReportTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportTemplateDTO reportTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportTemplate partially : {}, {}", id, reportTemplateDTO);
        if (reportTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportTemplateDTO> result = reportTemplateService.partialUpdate(reportTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reportTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-templates} : get all the reportTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportTemplateDTO>> getAllReportTemplates(
        ReportTemplateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportTemplates by criteria: {}", criteria);

        Page<ReportTemplateDTO> page = reportTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-templates/count} : count all the reportTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportTemplates(ReportTemplateCriteria criteria) {
        LOG.debug("REST request to count ReportTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-templates/:id} : get the "id" reportTemplate.
     *
     * @param id the id of the reportTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportTemplateDTO> getReportTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportTemplate : {}", id);
        Optional<ReportTemplateDTO> reportTemplateDTO = reportTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportTemplateDTO);
    }

    /**
     * {@code DELETE  /report-templates/:id} : delete the "id" reportTemplate.
     *
     * @param id the id of the reportTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportTemplate : {}", id);
        reportTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
