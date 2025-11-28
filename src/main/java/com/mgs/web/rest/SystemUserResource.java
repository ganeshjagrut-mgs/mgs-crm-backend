package com.mgs.web.rest;

import com.mgs.repository.SystemUserRepository;
import com.mgs.service.SystemUserQueryService;
import com.mgs.service.SystemUserService;
import com.mgs.service.criteria.SystemUserCriteria;
import com.mgs.service.dto.SystemUserDTO;
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
 * REST controller for managing {@link com.mgs.domain.SystemUser}.
 */
@RestController
@RequestMapping("/api/system-users")
public class SystemUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(SystemUserResource.class);

    private static final String ENTITY_NAME = "systemUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemUserService systemUserService;

    private final SystemUserRepository systemUserRepository;

    private final SystemUserQueryService systemUserQueryService;

    public SystemUserResource(
        SystemUserService systemUserService,
        SystemUserRepository systemUserRepository,
        SystemUserQueryService systemUserQueryService
    ) {
        this.systemUserService = systemUserService;
        this.systemUserRepository = systemUserRepository;
        this.systemUserQueryService = systemUserQueryService;
    }

    /**
     * {@code POST  /system-users} : Create a new systemUser.
     *
     * @param systemUserDTO the systemUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemUserDTO, or with status {@code 400 (Bad Request)} if the systemUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SystemUserDTO> createSystemUser(@Valid @RequestBody SystemUserDTO systemUserDTO) throws URISyntaxException {
        LOG.debug("REST request to save SystemUser : {}", systemUserDTO);
        if (systemUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemUserDTO = systemUserService.save(systemUserDTO);
        return ResponseEntity.created(new URI("/api/system-users/" + systemUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, systemUserDTO.getId().toString()))
            .body(systemUserDTO);
    }

    /**
     * {@code PUT  /system-users/:id} : Updates an existing systemUser.
     *
     * @param id the id of the systemUserDTO to save.
     * @param systemUserDTO the systemUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemUserDTO,
     * or with status {@code 400 (Bad Request)} if the systemUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SystemUserDTO> updateSystemUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemUserDTO systemUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SystemUser : {}, {}", id, systemUserDTO);
        if (systemUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemUserDTO = systemUserService.update(systemUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemUserDTO.getId().toString()))
            .body(systemUserDTO);
    }

    /**
     * {@code PATCH  /system-users/:id} : Partial updates given fields of an existing systemUser, field will ignore if it is null
     *
     * @param id the id of the systemUserDTO to save.
     * @param systemUserDTO the systemUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemUserDTO,
     * or with status {@code 400 (Bad Request)} if the systemUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemUserDTO> partialUpdateSystemUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemUserDTO systemUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SystemUser partially : {}, {}", id, systemUserDTO);
        if (systemUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemUserDTO> result = systemUserService.partialUpdate(systemUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-users} : get all the systemUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemUsers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SystemUserDTO>> getAllSystemUsers(
        SystemUserCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SystemUsers by criteria: {}", criteria);

        Page<SystemUserDTO> page = systemUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-users/count} : count all the systemUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSystemUsers(SystemUserCriteria criteria) {
        LOG.debug("REST request to count SystemUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /system-users/:id} : get the "id" systemUser.
     *
     * @param id the id of the systemUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemUserDTO> getSystemUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SystemUser : {}", id);
        Optional<SystemUserDTO> systemUserDTO = systemUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemUserDTO);
    }

    /**
     * {@code DELETE  /system-users/:id} : delete the "id" systemUser.
     *
     * @param id the id of the systemUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SystemUser : {}", id);
        systemUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
