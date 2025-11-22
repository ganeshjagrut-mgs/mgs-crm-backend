package com.crm.web.rest;

import com.crm.repository.TaskAuditRepository;
import com.crm.service.TaskAuditQueryService;
import com.crm.service.TaskAuditService;
import com.crm.service.criteria.TaskAuditCriteria;
import com.crm.service.dto.TaskAuditDTO;
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
 * REST controller for managing {@link com.crm.domain.TaskAudit}.
 */
@RestController
@RequestMapping("/api/task-audits")
public class TaskAuditResource {

    private final Logger log = LoggerFactory.getLogger(TaskAuditResource.class);

    private static final String ENTITY_NAME = "taskAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskAuditService taskAuditService;

    private final TaskAuditRepository taskAuditRepository;

    private final TaskAuditQueryService taskAuditQueryService;

    public TaskAuditResource(
        TaskAuditService taskAuditService,
        TaskAuditRepository taskAuditRepository,
        TaskAuditQueryService taskAuditQueryService
    ) {
        this.taskAuditService = taskAuditService;
        this.taskAuditRepository = taskAuditRepository;
        this.taskAuditQueryService = taskAuditQueryService;
    }

    /**
     * {@code POST  /task-audits} : Create a new taskAudit.
     *
     * @param taskAuditDTO the taskAuditDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskAuditDTO, or with status {@code 400 (Bad Request)} if the taskAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaskAuditDTO> createTaskAudit(@RequestBody TaskAuditDTO taskAuditDTO) throws URISyntaxException {
        log.debug("REST request to save TaskAudit : {}", taskAuditDTO);
        if (taskAuditDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskAuditDTO result = taskAuditService.save(taskAuditDTO);
        return ResponseEntity
            .created(new URI("/api/task-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-audits/:id} : Updates an existing taskAudit.
     *
     * @param id the id of the taskAuditDTO to save.
     * @param taskAuditDTO the taskAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskAuditDTO,
     * or with status {@code 400 (Bad Request)} if the taskAuditDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskAuditDTO> updateTaskAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskAuditDTO taskAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaskAudit : {}, {}", id, taskAuditDTO);
        if (taskAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskAuditDTO result = taskAuditService.update(taskAuditDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskAuditDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /task-audits/:id} : Partial updates given fields of an existing taskAudit, field will ignore if it is null
     *
     * @param id the id of the taskAuditDTO to save.
     * @param taskAuditDTO the taskAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskAuditDTO,
     * or with status {@code 400 (Bad Request)} if the taskAuditDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskAuditDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskAuditDTO> partialUpdateTaskAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskAuditDTO taskAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaskAudit partially : {}, {}", id, taskAuditDTO);
        if (taskAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskAuditDTO> result = taskAuditService.partialUpdate(taskAuditDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskAuditDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /task-audits} : get all the taskAudits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskAudits in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TaskAuditDTO>> getAllTaskAudits(
        TaskAuditCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TaskAudits by criteria: {}", criteria);

        Page<TaskAuditDTO> page = taskAuditQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /task-audits/count} : count all the taskAudits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTaskAudits(TaskAuditCriteria criteria) {
        log.debug("REST request to count TaskAudits by criteria: {}", criteria);
        return ResponseEntity.ok().body(taskAuditQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /task-audits/:id} : get the "id" taskAudit.
     *
     * @param id the id of the taskAuditDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskAuditDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskAuditDTO> getTaskAudit(@PathVariable("id") Long id) {
        log.debug("REST request to get TaskAudit : {}", id);
        Optional<TaskAuditDTO> taskAuditDTO = taskAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskAuditDTO);
    }

    /**
     * {@code DELETE  /task-audits/:id} : delete the "id" taskAudit.
     *
     * @param id the id of the taskAuditDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskAudit(@PathVariable("id") Long id) {
        log.debug("REST request to delete TaskAudit : {}", id);
        taskAuditService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
