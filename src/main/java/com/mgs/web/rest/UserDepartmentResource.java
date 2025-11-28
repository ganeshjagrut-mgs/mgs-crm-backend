package com.mgs.web.rest;

import com.mgs.repository.UserDepartmentRepository;
import com.mgs.service.UserDepartmentQueryService;
import com.mgs.service.UserDepartmentService;
import com.mgs.service.criteria.UserDepartmentCriteria;
import com.mgs.service.dto.UserDepartmentDTO;
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
 * REST controller for managing {@link com.mgs.domain.UserDepartment}.
 */
@RestController
@RequestMapping("/api/user-departments")
public class UserDepartmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserDepartmentResource.class);

    private static final String ENTITY_NAME = "userDepartment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDepartmentService userDepartmentService;

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserDepartmentQueryService userDepartmentQueryService;

    public UserDepartmentResource(
        UserDepartmentService userDepartmentService,
        UserDepartmentRepository userDepartmentRepository,
        UserDepartmentQueryService userDepartmentQueryService
    ) {
        this.userDepartmentService = userDepartmentService;
        this.userDepartmentRepository = userDepartmentRepository;
        this.userDepartmentQueryService = userDepartmentQueryService;
    }

    /**
     * {@code POST  /user-departments} : Create a new userDepartment.
     *
     * @param userDepartmentDTO the userDepartmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDepartmentDTO, or with status {@code 400 (Bad Request)} if the userDepartment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserDepartmentDTO> createUserDepartment(@Valid @RequestBody UserDepartmentDTO userDepartmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserDepartment : {}", userDepartmentDTO);
        if (userDepartmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new userDepartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userDepartmentDTO = userDepartmentService.save(userDepartmentDTO);
        return ResponseEntity.created(new URI("/api/user-departments/" + userDepartmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userDepartmentDTO.getId().toString()))
            .body(userDepartmentDTO);
    }

    /**
     * {@code PUT  /user-departments/:id} : Updates an existing userDepartment.
     *
     * @param id the id of the userDepartmentDTO to save.
     * @param userDepartmentDTO the userDepartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDepartmentDTO,
     * or with status {@code 400 (Bad Request)} if the userDepartmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDepartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDepartmentDTO> updateUserDepartment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserDepartmentDTO userDepartmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserDepartment : {}, {}", id, userDepartmentDTO);
        if (userDepartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDepartmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDepartmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userDepartmentDTO = userDepartmentService.update(userDepartmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDepartmentDTO.getId().toString()))
            .body(userDepartmentDTO);
    }

    /**
     * {@code PATCH  /user-departments/:id} : Partial updates given fields of an existing userDepartment, field will ignore if it is null
     *
     * @param id the id of the userDepartmentDTO to save.
     * @param userDepartmentDTO the userDepartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDepartmentDTO,
     * or with status {@code 400 (Bad Request)} if the userDepartmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userDepartmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDepartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserDepartmentDTO> partialUpdateUserDepartment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserDepartmentDTO userDepartmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserDepartment partially : {}, {}", id, userDepartmentDTO);
        if (userDepartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDepartmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDepartmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDepartmentDTO> result = userDepartmentService.partialUpdate(userDepartmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDepartmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-departments} : get all the userDepartments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDepartments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserDepartmentDTO>> getAllUserDepartments(
        UserDepartmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get UserDepartments by criteria: {}", criteria);

        Page<UserDepartmentDTO> page = userDepartmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-departments/count} : count all the userDepartments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserDepartments(UserDepartmentCriteria criteria) {
        LOG.debug("REST request to count UserDepartments by criteria: {}", criteria);
        return ResponseEntity.ok().body(userDepartmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-departments/:id} : get the "id" userDepartment.
     *
     * @param id the id of the userDepartmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDepartmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDepartmentDTO> getUserDepartment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserDepartment : {}", id);
        Optional<UserDepartmentDTO> userDepartmentDTO = userDepartmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDepartmentDTO);
    }

    /**
     * {@code DELETE  /user-departments/:id} : delete the "id" userDepartment.
     *
     * @param id the id of the userDepartmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDepartment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserDepartment : {}", id);
        userDepartmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
