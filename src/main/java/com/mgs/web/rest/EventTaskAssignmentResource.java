package com.mgs.web.rest;

import com.mgs.repository.EventTaskAssignmentRepository;
import com.mgs.service.EventTaskAssignmentQueryService;
import com.mgs.service.EventTaskAssignmentService;
import com.mgs.service.criteria.EventTaskAssignmentCriteria;
import com.mgs.service.dto.EventTaskAssignmentDTO;
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
 * REST controller for managing {@link com.mgs.domain.EventTaskAssignment}.
 */
@RestController
@RequestMapping("/api/event-task-assignments")
public class EventTaskAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(EventTaskAssignmentResource.class);

    private static final String ENTITY_NAME = "eventTaskAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTaskAssignmentService eventTaskAssignmentService;

    private final EventTaskAssignmentRepository eventTaskAssignmentRepository;

    private final EventTaskAssignmentQueryService eventTaskAssignmentQueryService;

    public EventTaskAssignmentResource(
        EventTaskAssignmentService eventTaskAssignmentService,
        EventTaskAssignmentRepository eventTaskAssignmentRepository,
        EventTaskAssignmentQueryService eventTaskAssignmentQueryService
    ) {
        this.eventTaskAssignmentService = eventTaskAssignmentService;
        this.eventTaskAssignmentRepository = eventTaskAssignmentRepository;
        this.eventTaskAssignmentQueryService = eventTaskAssignmentQueryService;
    }

    /**
     * {@code POST  /event-task-assignments} : Create a new eventTaskAssignment.
     *
     * @param eventTaskAssignmentDTO the eventTaskAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventTaskAssignmentDTO, or with status {@code 400 (Bad Request)} if the eventTaskAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventTaskAssignmentDTO> createEventTaskAssignment(
        @Valid @RequestBody EventTaskAssignmentDTO eventTaskAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EventTaskAssignment : {}", eventTaskAssignmentDTO);
        if (eventTaskAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventTaskAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        eventTaskAssignmentDTO = eventTaskAssignmentService.save(eventTaskAssignmentDTO);
        return ResponseEntity.created(new URI("/api/event-task-assignments/" + eventTaskAssignmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, eventTaskAssignmentDTO.getId().toString()))
            .body(eventTaskAssignmentDTO);
    }

    /**
     * {@code PUT  /event-task-assignments/:id} : Updates an existing eventTaskAssignment.
     *
     * @param id the id of the eventTaskAssignmentDTO to save.
     * @param eventTaskAssignmentDTO the eventTaskAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventTaskAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the eventTaskAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventTaskAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventTaskAssignmentDTO> updateEventTaskAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventTaskAssignmentDTO eventTaskAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EventTaskAssignment : {}, {}", id, eventTaskAssignmentDTO);
        if (eventTaskAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTaskAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTaskAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        eventTaskAssignmentDTO = eventTaskAssignmentService.update(eventTaskAssignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventTaskAssignmentDTO.getId().toString()))
            .body(eventTaskAssignmentDTO);
    }

    /**
     * {@code PATCH  /event-task-assignments/:id} : Partial updates given fields of an existing eventTaskAssignment, field will ignore if it is null
     *
     * @param id the id of the eventTaskAssignmentDTO to save.
     * @param eventTaskAssignmentDTO the eventTaskAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventTaskAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the eventTaskAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventTaskAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventTaskAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventTaskAssignmentDTO> partialUpdateEventTaskAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventTaskAssignmentDTO eventTaskAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EventTaskAssignment partially : {}, {}", id, eventTaskAssignmentDTO);
        if (eventTaskAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTaskAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTaskAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventTaskAssignmentDTO> result = eventTaskAssignmentService.partialUpdate(eventTaskAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventTaskAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-task-assignments} : get all the eventTaskAssignments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventTaskAssignments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventTaskAssignmentDTO>> getAllEventTaskAssignments(
        EventTaskAssignmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EventTaskAssignments by criteria: {}", criteria);

        Page<EventTaskAssignmentDTO> page = eventTaskAssignmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-task-assignments/count} : count all the eventTaskAssignments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventTaskAssignments(EventTaskAssignmentCriteria criteria) {
        LOG.debug("REST request to count EventTaskAssignments by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventTaskAssignmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-task-assignments/:id} : get the "id" eventTaskAssignment.
     *
     * @param id the id of the eventTaskAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventTaskAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventTaskAssignmentDTO> getEventTaskAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EventTaskAssignment : {}", id);
        Optional<EventTaskAssignmentDTO> eventTaskAssignmentDTO = eventTaskAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventTaskAssignmentDTO);
    }

    /**
     * {@code DELETE  /event-task-assignments/:id} : delete the "id" eventTaskAssignment.
     *
     * @param id the id of the eventTaskAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventTaskAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EventTaskAssignment : {}", id);
        eventTaskAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
