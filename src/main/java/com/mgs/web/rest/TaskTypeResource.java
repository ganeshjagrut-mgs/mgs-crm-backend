package com.mgs.web.rest;

import com.mgs.repository.TaskTypeRepository;
import com.mgs.service.TaskTypeQueryService;
import com.mgs.service.TaskTypeService;
import com.mgs.service.criteria.TaskTypeCriteria;
import com.mgs.service.dto.TaskTypeDTO;
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
 * REST controller for managing {@link com.mgs.domain.TaskType}.
 */
@RestController
@RequestMapping("/api/task-types")
public class TaskTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaskTypeResource.class);

    private static final String ENTITY_NAME = "taskType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskTypeService taskTypeService;

    private final TaskTypeRepository taskTypeRepository;

    private final TaskTypeQueryService taskTypeQueryService;

    public TaskTypeResource(
        TaskTypeService taskTypeService,
        TaskTypeRepository taskTypeRepository,
        TaskTypeQueryService taskTypeQueryService
    ) {
        this.taskTypeService = taskTypeService;
        this.taskTypeRepository = taskTypeRepository;
        this.taskTypeQueryService = taskTypeQueryService;
    }

    /**
     * {@code POST  /task-types} : Create a new taskType.
     *
     * @param taskTypeDTO the taskTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskTypeDTO, or with status {@code 400 (Bad Request)} if the taskType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaskTypeDTO> createTaskType(@Valid @RequestBody TaskTypeDTO taskTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save TaskType : {}", taskTypeDTO);
        if (taskTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taskTypeDTO = taskTypeService.save(taskTypeDTO);
        return ResponseEntity.created(new URI("/api/task-types/" + taskTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, taskTypeDTO.getId().toString()))
            .body(taskTypeDTO);
    }

    /**
     * {@code PUT  /task-types/:id} : Updates an existing taskType.
     *
     * @param id the id of the taskTypeDTO to save.
     * @param taskTypeDTO the taskTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskTypeDTO,
     * or with status {@code 400 (Bad Request)} if the taskTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskTypeDTO> updateTaskType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaskTypeDTO taskTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TaskType : {}, {}", id, taskTypeDTO);
        if (taskTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taskTypeDTO = taskTypeService.update(taskTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taskTypeDTO.getId().toString()))
            .body(taskTypeDTO);
    }

    /**
     * {@code PATCH  /task-types/:id} : Partial updates given fields of an existing taskType, field will ignore if it is null
     *
     * @param id the id of the taskTypeDTO to save.
     * @param taskTypeDTO the taskTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskTypeDTO,
     * or with status {@code 400 (Bad Request)} if the taskTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskTypeDTO> partialUpdateTaskType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaskTypeDTO taskTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TaskType partially : {}, {}", id, taskTypeDTO);
        if (taskTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskTypeDTO> result = taskTypeService.partialUpdate(taskTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taskTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /task-types} : get all the taskTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TaskTypeDTO>> getAllTaskTypes(
        TaskTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TaskTypes by criteria: {}", criteria);

        Page<TaskTypeDTO> page = taskTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /task-types/count} : count all the taskTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTaskTypes(TaskTypeCriteria criteria) {
        LOG.debug("REST request to count TaskTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(taskTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /task-types/:id} : get the "id" taskType.
     *
     * @param id the id of the taskTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskTypeDTO> getTaskType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TaskType : {}", id);
        Optional<TaskTypeDTO> taskTypeDTO = taskTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskTypeDTO);
    }

    /**
     * {@code DELETE  /task-types/:id} : delete the "id" taskType.
     *
     * @param id the id of the taskTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TaskType : {}", id);
        taskTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
